package com.app.pos.system.dto.response;

import com.app.pos.system.model.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
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