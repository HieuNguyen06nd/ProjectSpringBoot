package com.hieunguyen.shopstorev2.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String input;  // Email hoặc phone

    @NotBlank
    private String password;
}
