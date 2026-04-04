package com.app.pos.system.service;

import com.app.pos.system.dto.request.CreatePromotionRequest;
import com.app.pos.system.dto.response.PromotionResponse;
import com.app.pos.system.exception.AlreadyExistsException;
import com.app.pos.system.exception.BadRequestException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.PromotionMapper;
import com.app.pos.system.model.Product;
import com.app.pos.system.model.Promotion;
import com.app.pos.system.model.PromotionProduct;
import com.app.pos.system.model.PromotionProductId;
import com.app.pos.system.repo.ProductRepository;
import com.app.pos.system.repo.PromotionProductRepo;
import com.app.pos.system.repo.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionProductRepo promotionProductRepo;
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    private final PromotionMapper promotionMapper;

    public BigDecimal getActiveDiscount(Long productId, BigDecimal price) {
        return promotionProductRepo
                .findActivePromotionForProduct(productId, OffsetDateTime.now())
                .map(pp -> calculateDiscount(pp.getPromotion(), price))
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateDiscount(Promotion promotion, BigDecimal price) {
        return switch (promotion.getType()) {
            case PERCENTAGE -> price.multiply(promotion.getValue());
            case FIXED_AMOUNT -> promotion.getValue();
        };
    }


    @Transactional
    public PromotionResponse createPromotion(CreatePromotionRequest request){
        Product product = productRepository.getProductByProductId(request.getProductId())
                .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", "Product with id " + request.getProductId() + " not found"));

        if(promotionProductRepo.existsActivePromotionByProductId(request.getProductId())){
            throw new AlreadyExistsException("PROMOTION_ALREADY_EXISTS", "An active promotion for this product already exists");
        }

        Promotion promotion = promotionRepository.save(promotionMapper.toEntityFromCreatePromotionRequest(request));

        PromotionProduct promotionProduct = promotionProductRepo.save(new PromotionProduct(
                new PromotionProductId(promotion.getPromotionId(), product.getProductId()), product, promotion));

        return promotionMapper.toResponseFromPromotionProduct(promotionProduct);
    }


    @Transactional
    public void togglePromotionActive(Long promotionId, Boolean active){
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new NotFoundException("PROMOTION_NOT_FOUND", "Promotion with id " + promotionId + " not found"));

        if(promotion.getEndsAt().isBefore(OffsetDateTime.now())){
            throw new BadRequestException("EXPIRED_PROMOTION", "Cannot activate or deactivate an expired promotion");
        }

        promotion.setActive(active);
        promotionRepository.save(promotion);
    }
}
