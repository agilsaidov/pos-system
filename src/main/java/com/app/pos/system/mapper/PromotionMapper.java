package com.app.pos.system.mapper;

import com.app.pos.system.dto.request.CreatePromotionRequest;
import com.app.pos.system.dto.response.PromotionResponse;
import com.app.pos.system.model.Promotion;
import com.app.pos.system.model.PromotionProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    Promotion toEntityFromCreatePromotionRequest(CreatePromotionRequest request);

    @Mapping(target = "productName", source = "promotionProduct.product.name")
    @Mapping(target = "promotionId", source = "promotionProduct.promotion.promotionId")
    @Mapping(target = "promotionName", source = "promotionProduct.promotion.name")
    @Mapping(target = "promoType", source = "promotionProduct.promotion.type")
    @Mapping(target = "discountValue", source = "promotionProduct.promotion.value")
    @Mapping(target = "active", source = "promotionProduct.promotion.active")
    @Mapping(target = "startsAt", source = "promotionProduct.promotion.startsAt")
    @Mapping(target = "endsAt", source = "promotionProduct.promotion.endsAt")
    PromotionResponse toResponseFromPromotionProduct(PromotionProduct promotionProduct);
}
