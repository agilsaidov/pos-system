package com.app.pos.system.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserResponse {
    private String userId;
    private String username;
    private String fullName;
    private Boolean enabled;
    private OffsetDateTime createdAt;
}
