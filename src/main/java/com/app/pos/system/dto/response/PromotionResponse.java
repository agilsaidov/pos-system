package com.app.pos.system.dto.response;

import com.app.pos.system.model.enums.PromoType;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter @Getter
public class PromotionResponse {
    private Long promotionId;
    private String promotionName;
    private PromoType promoType;
    private Double discountValue;
    private Boolean active;
    private OffsetDateTime startsAt;
    private OffsetDateTime endsAt;
}
