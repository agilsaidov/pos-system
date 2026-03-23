package com.app.pos.system.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetailedCashierReportResponse {
    private Long cashierId;
    private String firstName;
    private String lastName;
    private Long saleCount;
    private BigDecimal revenue;
    private BigDecimal discountTotal;
    private BigDecimal taxTotal;
    private BigDecimal averageSaleValue;
}
