package com.app.pos.system.service;

import com.app.pos.system.dto.request.CheckoutItemRequest;
import com.app.pos.system.dto.request.CheckoutRequest;
import com.app.pos.system.dto.response.CheckoutResponse;
import com.app.pos.system.dto.response.SaleItemResponse;
import com.app.pos.system.dto.response.SaleResponse;
import com.app.pos.system.exception.AccessDeniedException;
import com.app.pos.system.exception.BadRequestException;
import com.app.pos.system.exception.ForbiddenException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.SaleMapper;
import com.app.pos.system.model.*;
import com.app.pos.system.model.enums.SaleStatus;
import com.app.pos.system.model.enums.StockMovementType;
import com.app.pos.system.repo.*;
import com.app.pos.system.specification.SaleSpec;
import com.app.pos.system.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final StoreAssignmentRepository storeAssignmentRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final PromotionService promotionService;
    private final StockMovementRepository stockMovementRepository;
    private final SaleItemRepository saleItemRepository;
    private final PaymentRepository paymentRepository;
    private final AuthUtils authUtils;



    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        UUID keycloakId = authUtils.getCurrentUserKeycloakId();
        User cashier = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("CASHIER_NOT_FOUND", "Cashier not found"));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new NotFoundException("STORE_NOT_FOUND", "Store not found"));

        if(!store.getActive()){
            throw new BadRequestException("STORE_INACTIVE", "Store is not active");
        }

        if (!storeAssignmentRepository.existsById(new StoreAssignmentId(cashier.getUserId(), store.getStoreId()))) {
            throw new AccessDeniedException("Cashier is not assigned to this store");
        }

        // Pre-fetch and Sort
        List<CheckoutItemRequest> sortedItems = request.getItems().stream()
                .sorted(Comparator.comparing(CheckoutItemRequest::getProductId))
                .toList();

        List<Long> productIds = sortedItems.stream().map(CheckoutItemRequest::getProductId).toList();

        Map<Long, Product> products = productRepository.findAllById(productIds)
                .stream().collect(Collectors.toMap(Product::getProductId, p -> p));


        Map<Long, Promotion> activePromotions = promotionService.getActivePromotionsForProducts(productIds);

        // Prepare Sale Header
        Sale sale = Sale.builder()
                .store(store)
                .cashier(cashier)
                .receiptNo(generateReceiptNo(store.getStoreId()))
                .status(SaleStatus.PENDING)
                .subTotal(BigDecimal.ZERO)
                .taxTotal(BigDecimal.ZERO)
                .discountTotal(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();

        Sale savedSale = saleRepository.save(sale);

        // Accumulators
        BigDecimal subTotalAcc = BigDecimal.ZERO;
        BigDecimal taxTotalAcc = BigDecimal.ZERO;
        BigDecimal discountTotalAcc = BigDecimal.ZERO;
        BigDecimal finalPayableAcc = BigDecimal.ZERO;

        List<SaleItem> saleItems = new ArrayList<>();
        List<Inventory> updatedInventories = new ArrayList<>();
        List<StockMovement> movements = new ArrayList<>();


        for (CheckoutItemRequest item : sortedItems) {
            Product product = products.get(item.getProductId());

            if (product == null || !product.getActive()) {
                throw new BadRequestException("PRODUCT_UNAVAILABLE", "Product is unavailable");
            }

            // Lock inventory and capture state immediately
            Inventory inventory = inventoryRepository
                    .findByIdWithLock(new InventoryId(store.getStoreId(), item.getProductId()))
                    .orElseThrow(() -> new NotFoundException("INVENTORY_NOT_FOUND", "No inventory for: " + product.getName()));

            if (inventory.getQuantity() < item.getQuantity()) {
                throw new BadRequestException("INSUFFICIENT_STOCK", "Insufficient stock for: " + product.getName());
            }

            int qtyBefore = inventory.getQuantity();
            int qtyAfter = qtyBefore - item.getQuantity();

            // Calculations
            BigDecimal unitPrice = product.getPrice();
            BigDecimal grossLine = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

            // Use pre-fetched promotion for calculation and auditing
            Promotion appliedPromo = activePromotions.get(product.getProductId());
            BigDecimal discountAmount = promotionService.calculateDiscountValue(unitPrice, item.getQuantity(), appliedPromo);

            BigDecimal netLine = grossLine.subtract(discountAmount);
            BigDecimal lineTax = netLine.multiply(product.getTaxRate()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal lineTotal = netLine.add(lineTax).setScale(2, RoundingMode.HALF_UP);

            // Accumulate
            subTotalAcc = subTotalAcc.add(grossLine);
            discountTotalAcc = discountTotalAcc.add(discountAmount);
            taxTotalAcc = taxTotalAcc.add(lineTax);
            finalPayableAcc = finalPayableAcc.add(lineTotal);

            inventory.setQuantity(qtyAfter);
            updatedInventories.add(inventory);

            saleItems.add(SaleItem.builder()
                    .sale(savedSale)
                    .product(product)
                    .appliedPromotion(appliedPromo)
                    .quantity(item.getQuantity())
                    .unitPriceSnapshot(unitPrice)
                    .taxRateSnapshot(product.getTaxRate())
                    .discountAmount(discountAmount)
                    .lineTotal(lineTotal)
                    .build());

            movements.add(StockMovement.builder()
                    .store(store)
                    .product(product)
                    .user(cashier)
                    .stockMovementType(StockMovementType.SALE)
                    .qtyBefore(qtyBefore)
                    .qtyDelta(-item.getQuantity())
                    .qtyAfter(qtyAfter)
                    .reason("Sale: " + savedSale.getReceiptNo())
                    .build());
        }

        // Finalize
        BigDecimal total = finalPayableAcc.setScale(2, RoundingMode.HALF_UP);
        if (request.getAmountTendered().compareTo(total) < 0) {
            throw new BadRequestException("INSUFFICIENT_PAYMENT", "Tendered amount is too low");
        }


        saleItemRepository.saveAll(saleItems);
        inventoryRepository.saveAll(updatedInventories);
        stockMovementRepository.saveAll(movements);

        savedSale.setSubTotal(subTotalAcc);
        savedSale.setTaxTotal(taxTotalAcc);
        savedSale.setDiscountTotal(discountTotalAcc);
        savedSale.setTotal(total);
        savedSale.setStatus(SaleStatus.COMPLETED);
        saleRepository.save(savedSale);

        Payment payment = new Payment();
        payment.setSale(savedSale);
        payment.setMethod(request.getPaymentMethod());
        payment.setAmountTendered(request.getAmountTendered());
        payment.setChangeGiven(request.getAmountTendered().subtract(total).setScale(2, RoundingMode.HALF_UP));
        payment.setAmount(total);
        paymentRepository.save(payment);

        CheckoutResponse response = saleMapper.toCheckoutResponse(savedSale);
        response.setChange(request.getAmountTendered().subtract(total).setScale(2, RoundingMode.HALF_UP));
        response.setAmountTendered(request.getAmountTendered());
        response.setPaymentMethod(request.getPaymentMethod());
        response.setItems(saleItems.stream().map(saleMapper::toSaleItemResponse).toList());

        return response;
    }


    public Page<SaleResponse> getSales(Long saleId, Long cashierId, Long storeId,
                                       OffsetDateTime from, OffsetDateTime to,
                                       int page, int size){

        if(storeId == null && authUtils.isManager()){
            throw new BadRequestException("STORE_ID_REQUIRED", "Managers must specify a storeId");
        }

        if(storeId != null) {
            validateStoreAccess(storeId);
        }

        Pageable pageable = PageRequest.of(page,size);

        return saleRepository.findAll(
                SaleSpec.withFilters(saleId, cashierId, storeId, from, to), pageable)
                .map(saleMapper::toResponse);
    }



    public Page<SaleResponse> getSalesForCashier(Long saleId, Long storeId,
                                       OffsetDateTime from, OffsetDateTime to,
                                       int page, int size){

        UUID cashierId = authUtils.getCurrentUserKeycloakId();
        User cashier = userRepository.findByKeycloakId(cashierId)
                .orElseThrow(() -> new NotFoundException("CASHIER_NOT_FOUND", "Cashier not found"));

        Pageable pageable = PageRequest.of(page,size);

        return saleRepository.findAll(
                        SaleSpec.withFilters(saleId, cashier.getUserId(), storeId, from, to), pageable)
                .map(saleMapper::toResponse);
    }


    private void validateStoreAccess(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        if(authUtils.isManager()) {
            UUID keycloakId = authUtils.getCurrentUserKeycloakId();
            User manager = userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new NotFoundException("MANAGER_NOT_FOUND", "Manager not found"));

            if (!storeAssignmentRepository.existsById(new StoreAssignmentId(manager.getUserId(), storeId))) {
                throw new AccessDeniedException("Manager with id " + manager.getUserId() + " does not have access to store " + storeId);
            }
        }
    }


    private String generateReceiptNo(Long storeId) {
        long receiptNo = saleRepository.getNextReceiptNo();
        return String.format("RCP-%d-%05d", storeId, receiptNo);
    }

}
