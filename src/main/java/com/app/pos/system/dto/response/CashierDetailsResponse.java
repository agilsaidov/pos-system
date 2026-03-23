package com.app.pos.system.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class CashierDetailsResponse {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean enabled;
    private Instant createdAt;
    private Long storeId;
    private String storeName;
    private String roleName;
}
