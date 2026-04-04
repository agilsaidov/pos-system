package com.app.pos.system.dto.request;

import com.app.pos.system.model.enums.PromoType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class CreatePromotionRequest {
    private Long productId;
    private String name;
    private PromoType type;
    private BigDecimal value;
    private OffsetDateTime startsAt;
    private OffsetDateTime endsAt;
    private Boolean active;
}
