package com.hieunguyen.dto.response;

import com.hieunguyen.utils.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;
}
