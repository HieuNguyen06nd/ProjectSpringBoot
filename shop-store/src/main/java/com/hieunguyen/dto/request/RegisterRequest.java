package com.hieunguyen.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {

    private String email;

    private String password;

    private String fullName;
}
