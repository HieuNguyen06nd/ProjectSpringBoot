package com.hieunguyen.lakeSide.dto.response;

import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwt;

    private String userId;

    private String userRole;
}
