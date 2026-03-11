package com.app.pos.system.projection;

import java.math.BigDecimal;

public interface DailyReportProjection {
    Long getStoreId();
    String getStoreName();
    BigDecimal getRevenue();
    BigDecimal getTaxTotal();
    BigDecimal getDiscountTotal();
    BigDecimal getSalesTotal();
}
