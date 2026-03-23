package com.app.pos.system.dto.response;

import com.app.pos.system.model.enums.StockMovementType;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class StockMovementResponse {
    private Long id;
    private String storeName;
    private String productName;
    private String productBarcode;
    private StockMovementType type;
    private Integer qtyDelta;
    private Integer qtyBefore;
    private Integer qtyAfter;
    private String reason;
    private String firstName;
    private String lastName;
    private OffsetDateTime createdAt;
}
