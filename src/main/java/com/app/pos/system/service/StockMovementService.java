package com.app.pos.system.service;

import com.app.pos.system.dto.request.StockMovementRequest;
import com.app.pos.system.dto.response.StockMovementResponse;
import com.app.pos.system.exception.AccessDeniedException;
import com.app.pos.system.exception.BadRequestException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.StockMovementMapper;
import com.app.pos.system.model.*;
import com.app.pos.system.model.enums.StockMovementType;
import com.app.pos.system.repo.*;
import com.app.pos.system.specification.StockMovementSpec;
import com.app.pos.system.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final StockMovementMapper stockMovementMapper;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StoreAssignmentRepository storeAssignmentRepository;
    private final InventoryRepository inventoryRepository;

    private final AuthUtils authUtils;

    public Page<StockMovementResponse> getStockMovement(Long storeId,
                                                        Long productId,
                                                        OffsetDateTime from,
                                                        OffsetDateTime to,
                                                        int page, int size){

        if(storeId != null){
            validateStoreAccess(storeId);

        }else if(authUtils.isManager()){
            throw new BadRequestException("STORE_ID_REQUIRED", "Managers must specify a storeId");
        }

        Pageable pageable = PageRequest.of(page,size);

        return stockMovementRepository.findAll(
                StockMovementSpec.withFilters(storeId, productId, from, to),pageable)
                .map(stockMovementMapper::toResponse);

    }


    @Transactional
    public StockMovementResponse createStockMovement(StockMovementRequest request){

        User currentUser = validateStoreAccess(request.getStoreId());

        if (request.getType() == StockMovementType.SALE ||
                request.getType() == StockMovementType.RETURN) {
            throw new BadRequestException("INVALID_TYPE",
                    "SALE and RETURN movements are created automatically by the system");
        }


        if (!productRepository.existsById(request.getProductId())) {
            throw new NotFoundException("PRODUCT_NOT_FOUND", "Product not found");
        }


        Inventory inventory = inventoryRepository
                .findById(new InventoryId(request.getStoreId(), request.getProductId()))
                .orElseThrow(() -> new NotFoundException("INVENTORY_NOT_FOUND",
                        "Product not found in this store's inventory"));

        int qtyBefore = inventory.getQuantity();
        int qtyDelta = request.getType() == StockMovementType.OUT
                ? -request.getQuantity()
                : request.getQuantity();
        int qtyAfter = qtyBefore + qtyDelta;

        if (qtyAfter < 0) {
            throw new BadRequestException("INSUFFICIENT_STOCK",
                    "Not enough stock. Current quantity: " + qtyBefore);
        }

        inventory.setQuantity(qtyAfter);
        inventoryRepository.save(inventory);


        StockMovement stockMovement = StockMovement.builder()
                .store(storeRepository.getReferenceById(request.getStoreId()))
                .product(productRepository.getReferenceById(request.getProductId()))
                .user(currentUser)
                .stockMovementType(request.getType())
                .qtyDelta(qtyDelta)
                .qtyBefore(qtyBefore)
                .qtyAfter(qtyAfter)
                .reason(request.getReason())
                .build();


        return stockMovementMapper.toResponse(stockMovementRepository.save(stockMovement));
    }


    private User validateStoreAccess(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new NotFoundException("STORE_NOT_FOUND", "Store not found");
        }

        UUID keycloakId = authUtils.getCurrentUserKeycloakId();
        User currentUser = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User not found"));

        if (!authUtils.isAdmin()) {
            boolean isAssigned = storeAssignmentRepository
                    .existsById(new StoreAssignmentId(currentUser.getUserId(), storeId));

            if (!isAssigned) {
                throw new AccessDeniedException("You do not have permission to access this store.");
            }
        }

        return currentUser;
    }
}
