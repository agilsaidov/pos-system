package com.app.pos.system.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class SaleResponse {
    private Long cashierId;
    private String cashierName;
    private Long storeId;
    private String storeName;
    private BigDecimal total;
    private OffsetDateTime issueDate;
}
