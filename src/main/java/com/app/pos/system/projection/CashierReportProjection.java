package com.app.pos.system.projection;

import java.math.BigDecimal;

public interface CashierReportProjection {
    Long getCashierId();
    String getFirstName();
    String getLastName();
    Long getSaleCount();
    BigDecimal getRevenue();
    BigDecimal getDiscountTotal();
    BigDecimal getTaxTotal();
}
