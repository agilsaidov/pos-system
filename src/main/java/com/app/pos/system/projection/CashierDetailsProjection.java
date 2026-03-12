package com.app.pos.system.projection;

import java.time.Instant;

public interface CashierDetailsProjection {
    Long getUserId();
    String getUsername();
    String getFullName();
    Boolean getEnabled();
    Instant getCreatedAt();
    Long getStoreId();
    String getStoreName();
    String getRoleName();
}
