package com.hieunguyen.shopstorev2.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
}
