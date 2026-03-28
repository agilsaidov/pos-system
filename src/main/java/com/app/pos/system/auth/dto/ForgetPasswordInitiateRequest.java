package com.app.pos.system.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgetPasswordInitiateRequest {
    @NotBlank(message = "Field 'input' is required")
    private String input;
}
