package com.app.pos.system.dto.response;

@Getter @Setter
public class SaleItemResponse {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
    private BigDecimal discountAmount;
    private BigDecimal lineTotal;
}