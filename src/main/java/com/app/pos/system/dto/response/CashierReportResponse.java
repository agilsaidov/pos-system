package com.app.pos.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CashierReportResponse {
    private Long cashierId;
    private String firstName;
    private String lastName;
    private Long saleCount;
    private BigDecimal revenue;
    private BigDecimal discountTotal;
    private BigDecimal taxTotal;
}
