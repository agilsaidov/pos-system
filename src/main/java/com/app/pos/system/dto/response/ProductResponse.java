package com.app.pos.system.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class ProductResponse {
    private Long productId;
    private String name;
    private String barcode;
    private BigDecimal price;
    private BigDecimal cost;
    private BigDecimal taxRate;
    private Boolean active;
    private OffsetDateTime createdAt;
}
