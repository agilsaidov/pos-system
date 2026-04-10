package com.app.pos.system.dto.response;

@Getter @Setter
public class CheckoutResponse {
    private Long saleId;
    private String receiptNo;
    private BigDecimal subTotal;
    private BigDecimal taxTotal;
    private BigDecimal discountTotal;
    private BigDecimal total;
    private BigDecimal amountTendered;
    private BigDecimal change;
    private PaymentMethod paymentMethod;
    private OffsetDateTime createdAt;
    private List<SaleItemResponse> items;
}