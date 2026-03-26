package com.app.pos.system.projection;

import java.time.Instant;

public interface CashierDetailsProjection {
    Long getUserId();
    String getUsername();
    Boolean getEnabled();
    Instant getCreatedAt();
    Long getStoreId();
    String getStoreName();
    String getRoleName();
}
