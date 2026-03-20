package com.app.pos.system.dto.request;

import com.app.pos.system.model.enums.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;

@Data
public class CreateUserRequest {

    @NotNull(message = "Field 'username' is required")
    private String username;

    @NotNull(message = "Field 'firstName' is required")
    private String firstName;

    @NotNull(message = "Field 'lastName' is required")
    private String lastName;

    @NotNull(message = "Field 'role' is required")
    private RoleName role;

    @NotNull(message = "Field 'enabled' is required")
    private Boolean enabled;

    @NotNull(message = "Field 'password' is required")
    private String password;
}
