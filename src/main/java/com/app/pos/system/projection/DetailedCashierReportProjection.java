package com.app.pos.system.projection;

import java.math.BigDecimal;

public interface DetailedCashierReportProjection {
    Long getCashierId();
    String getFirstName();
    String getLastName();
    Long getSaleCount();
    BigDecimal getRevenue();
    BigDecimal getDiscountTotal();
    BigDecimal getTaxTotal();
    BigDecimal getAverageSaleValue();
}
