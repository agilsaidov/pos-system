package com.app.pos.system.dto.response;

import com.app.pos.system.model.enums.PromoType;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PromotionResponse {
    private Long promotionId;
    private String promotionName;
    private String productName;
    private PromoType promoType;
    private Double discountValue;
    private Boolean active;
    private OffsetDateTime startsAt;
    private OffsetDateTime endsAt;
}
