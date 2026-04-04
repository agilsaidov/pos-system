package com.app.pos.system.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductLookupResponse {
    private Long productId;
    private String barcode;
    private String name;
    private BigDecimal taxRate;
    private BigDecimal discountAmount;
    private BigDecimal finalPrice;
}
