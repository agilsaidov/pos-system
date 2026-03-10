package com.app.pos.system.projection;

import java.math.BigDecimal;

public interface CashierReportProjection {
    Long getCashierId();
    String getCashierName();
    Long getSaleCount();
    BigDecimal getRevenue();
    BigDecimal getDiscountTotal();
    BigDecimal getTaxTotal();
}
