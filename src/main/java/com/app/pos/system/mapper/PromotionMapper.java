package com.app.pos.system.mapper;

import com.app.pos.system.dto.request.CreatePromotionRequest;
import com.app.pos.system.dto.response.PromotionProductResponse;
import com.app.pos.system.dto.response.PromotionResponse;
import com.app.pos.system.dto.response.PromotionWithProductsResponse;
import com.app.pos.system.model.Product;
import com.app.pos.system.model.Promotion;
import com.app.pos.system.model.PromotionProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    Promotion toEntityFromCreatePromotionRequest(CreatePromotionRequest request);

    @Mapping(target = "promotionId", source = "promotion.promotionId")
    @Mapping(target = "promotionName", source = "promotion.name")
    @Mapping(target = "promoType", source = "promotion.type")
    @Mapping(target = "discountValue", source = "promotion.value")
    @Mapping(target = "active", source = "promotion.active")
    @Mapping(target = "startsAt", source = "promotion.startsAt")
    @Mapping(target = "endsAt", source = "promotion.endsAt")
    PromotionResponse toResponse(Promotion promotion);

    @Mapping(target = "productId", source = "promotionProduct.product.productId")
    @Mapping(target = "productName", source = "promotionProduct.product.name")
    @Mapping(target = "barcode", source = "promotionProduct.product.barcode")
    @Mapping(target = "active", source = "promotionProduct.active")
    PromotionProductResponse toPromotionProductResponse(PromotionProduct promotionProduct);

    @Mapping(target = "promotionId", source = "promotion.promotionId")
    @Mapping(target = "promotionName", source = "promotion.name")
    @Mapping(target = "promoType", source = "promotion.type")
    @Mapping(target = "discountValue", source = "promotion.value")
    @Mapping(target = "active", source = "promotion.active")
    @Mapping(target = "startsAt", source = "promotion.startsAt")
    @Mapping(target = "endsAt", source = "promotion.endsAt")
    PromotionWithProductsResponse toPromotionWithProductsResponse(Promotion promotion);
}
