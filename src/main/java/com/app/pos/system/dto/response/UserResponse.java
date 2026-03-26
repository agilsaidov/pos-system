package com.app.pos.system.dto.response;

import com.app.pos.system.model.enums.RoleName;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class UserResponse {
    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;
    private List<RoleName> roles;
    private OffsetDateTime createdAt;
}
