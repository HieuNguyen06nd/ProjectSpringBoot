package com.hieunguyen.shopstorev2.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String tokenType; // thường là "Bearer"
    private long expiresIn;   // thời gian hết hạn tính bằng giây
}
