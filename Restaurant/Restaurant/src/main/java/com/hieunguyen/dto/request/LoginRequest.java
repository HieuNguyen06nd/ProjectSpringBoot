package com.hieunguyen.dto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "Identifier (email or phone) is required")
    private String identifier;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "OTP is required")
    private String otp;
}
