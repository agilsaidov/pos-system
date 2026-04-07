package com.app.pos.system.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class AddProductsToPromotionRequest {
    @NotEmpty(message = "Product list can not be empty")
    List<Long> productIds;
}
