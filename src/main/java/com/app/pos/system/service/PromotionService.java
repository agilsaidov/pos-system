package com.app.pos.system.service;

import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.model.Promotion;
import com.app.pos.system.model.PromotionProduct;
import com.app.pos.system.repo.PromotionProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionProductRepo promotionProductRepo;

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
}
