package com.app.pos.system.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter @Setter
public class InventoryResponse {

    private Long storeId;
    private String storeName;
    private Long productId;
    private String productName;
    private String productBarcode;
    private Integer quantity;
    private OffsetDateTime updatedAt;
}
