package com.app.pos.system.dto.response;

import com.app.pos.system.model.enums.PromoType;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter @Setter
public class PromotionWithProductsResponse {

    private Long promotionId;
    private String promotionName;
    private PromoType promoType;
    private Double discountValue;
    private OffsetDateTime startsAt;
    private OffsetDateTime endsAt;
    private Boolean active;

    List<PromotionProductResponse> promotionProducts;
}
