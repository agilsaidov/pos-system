package com.app.pos.system.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgetPasswordValidateRequest {

    @NotBlank(message = "Field 'email' is required")
    private String email;

    @NotBlank(message = "Field 'otp' is required")
    private String otp;

    @NotBlank(message = "Field 'password' is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$",
            message = "Password must contain at least one letter, one number and one special character"
    )
    private String password;

    @NotBlank(message = "confirmPassword is required")
    private String confirmPassword;
}
