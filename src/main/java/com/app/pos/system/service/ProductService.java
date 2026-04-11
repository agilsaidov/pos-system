package com.app.pos.system.service;

import com.app.pos.system.dto.request.ProductRequest;
import com.app.pos.system.dto.response.ProductLookupResponse;
import com.app.pos.system.dto.response.ProductResponse;
import com.app.pos.system.exception.BadRequestException;
import com.app.pos.system.exception.DuplicateException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.ProductMapper;
import com.app.pos.system.model.Product;
import com.app.pos.system.model.Promotion;
import com.app.pos.system.model.PromotionProduct;
import com.app.pos.system.repo.ProductRepository;
import com.app.pos.system.repo.PromotionProductRepo;
import com.app.pos.system.repo.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final ProductMapper productMapper;
    private final PromotionService promotionService;
    private final PromotionProductRepo promotionProductRepo;

    public ProductLookupResponse lookup(String barcode){
        Product product = productRepo.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException(
                        "PRODUCT_NOT_FOUND",
                        "No product found with barcode: " + barcode)
                );

        if(!product.getActive()){
            throw new BadRequestException(
                    "INACTIVE_PRODUCT",
                    "Product with barcode " + barcode + " is inactive and can't be sold"
            );
        }

        Optional<PromotionProduct> promotionProduct = promotionProductRepo
                .findActivePromotionForProduct(product.getProductId(), OffsetDateTime.now());

        BigDecimal discountAmount =  promotionService.getActiveDiscount(promotionProduct, product.getPrice());
        BigDecimal finalPrice = product.getPrice().subtract(discountAmount);

        ProductLookupResponse response = productMapper.toLookupResponse(product);

        if(promotionProduct.isPresent()) {
            response.setPromotionId(promotionProduct.get().getPromotion().getPromotionId());
            response.setPromotionName(promotionProduct.get().getPromotion().getName());
        }
        response.setDiscountAmount(discountAmount);
        response.setFinalPrice(finalPrice);

        return response;
    }

    public ProductResponse createProduct(ProductRequest request){

        if(productRepo.existsByBarcode(request.getBarcode())){
            throw new DuplicateException(
                    "DUPLICATE_PRODUCT",
                    "Product with barcode " + request.getBarcode() + " already exists"
            );
        }

        Product saved = productRepo.save(productMapper.toEntity(request));
        return productMapper.toResponse(saved);
    }


    public ProductResponse updateProduct(Long id, ProductRequest request){
        Product product = productRepo.getProductByProductId(id)
                .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", "Product with id " + id + " not found"));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCost(request.getCost());
        product.setTaxRate(request.getTaxRate());
        product.setActive(request.getActive());

        return productMapper.toResponse(product);
    }


    public Page<ProductResponse> getProducts(String search, String barcode, int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        if(barcode != null && !barcode.isBlank()){
            Product product = productRepo.findByBarcode(barcode)
                    .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND",
                            "Product with barcode " + barcode + " not found"));

            return new PageImpl<>(List.of(productMapper.toResponse(product)), pageable, 1);

        }
        else if(search != null && !search.isBlank()){
            return productRepo.findByNameContainingIgnoreCase(search, pageable)
                    .map(product -> productMapper.toResponse(product));
        }
        else{
            return productRepo.findAll(pageable)
                    .map(product -> productMapper.toResponse(product));
        }
    }


    public void toggleProductActive(Long productId, Boolean active){

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        "PRODUCT_NOT_FOUND",
                        "Product with id " + productId + " not found")
                );

        product.setActive(active);
        productRepo.save(product);
    }
}
