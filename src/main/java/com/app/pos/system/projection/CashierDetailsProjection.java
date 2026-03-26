package com.app.pos.system.projection;

import java.time.Instant;

public interface CashierDetailsProjection {
    Long getUserId();
    String getUsername();
    String getEmail();
    String getFirstName();
    String getLastName();
    Boolean getEnabled();
    Instant getCreatedAt();
    Long getStoreId();
    String getStoreName();
    String getRoleName();
}
