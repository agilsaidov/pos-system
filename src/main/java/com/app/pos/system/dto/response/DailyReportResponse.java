package com.app.pos.system.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DailyReportResponse {
    private Long storeId;
    private String storeName;
    private BigDecimal revenue;
    private BigDecimal taxTotal;
    private BigDecimal discountTotal;
    private BigDecimal salesTotal;
}
