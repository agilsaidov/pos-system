package com.app.pos.system.dto.request;

import com.app.pos.system.model.enums.PromoType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class CreatePromotionRequest {

    @NotBlank(message = "Field 'name' is required")
    @Size(max = 200, message = "Promotion name cannot exceed 200 characters")
    private String name;

    @NotBlank(message = "Field 'type' is required")
    private PromoType type;

    @NotBlank(message = "Field 'value' is required")
    private BigDecimal value;

    @NotBlank(message = "Field 'startsAt' is required")
    private OffsetDateTime startsAt;

    @NotBlank(message = "Field 'endsAt' is required")
    private OffsetDateTime endsAt;

    @NotBlank(message = "Field 'active' is required")
    private Boolean active;
}
