package com.app.pos.system.dto.request;

import com.app.pos.system.model.enums.StockMovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockMovementRequest {

    @NotNull(message = "Manager id is required")
    private Long managerId;

    @NotNull(message = "Store is required")
    private Long storeId;

    @NotNull(message = "Product is required")
    private Long productId;

    @NotNull(message = "Movement type is required")
    private StockMovementType type;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;
}