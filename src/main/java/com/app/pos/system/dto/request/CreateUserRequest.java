package com.app.pos.system.dto.request;

import com.app.pos.system.model.enums.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    private String username;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    @NotNull(message = "Field 'role' is required")
    private RoleName role;

    @NotNull(message = "Field 'enabled' is required")
    private Boolean enabled;

    @NotNull(message = "Field 'password' is required")
    @Size(max = 30, min = 6, message = "Password must be between 6 and 30 elements")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[^a-zA-Z\\\\d]).+$",
             message = "Password must have symbol/upper/lower-case letters and digit")
    private String password;
}
