package com.app.pos.system.dto.request;

import com.app.pos.system.model.enums.PromoType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter @Setter
public class UpdatePromotionRequest {

    @NotBlank(message = "Field 'name' is required")
    @Size(max = 200, message = "Promotion name cannot exceed 200 characters")
    private String name;

    @NotNull(message = "Field 'type' is required")
    private PromoType type;

    @NotNull(message = "Field 'value' is required")
    private BigDecimal value;

    @NotNull(message = "Field 'startsAt' is required")
    private OffsetDateTime startsAt;

    @NotNull(message = "Field 'endsAt' is required")
    private OffsetDateTime endsAt;

}
