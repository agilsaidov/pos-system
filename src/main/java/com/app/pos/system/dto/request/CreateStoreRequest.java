package com.app.pos.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter @Setter
public class CreateStoreRequest {

    @NotBlank(message = "Field 'name' is required")
    @Size(max = 200, message = "'name' cannot exceed 200 characters")
    private String name;

    @NotBlank(message = "Field 'city' is required")
    @Size(max = 100, message = "'city' cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "Field 'city' is required")
    @Size(max = 500, message = "'city' cannot exceed 100 characters")
    private String address;

    @NotBlank(message = "Field 'city' is required")
    @Size(max = 50, message = "'city' cannot exceed 100 characters")
    private String phone;

    @NotNull(message = "Field 'city' is required")
    private OffsetDateTime openedAt;
}
