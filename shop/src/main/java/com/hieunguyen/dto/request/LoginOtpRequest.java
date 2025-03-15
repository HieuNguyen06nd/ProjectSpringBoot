package com.hieunguyen.dto.request;

import com.hieunguyen.utils.USER_ROLE;
import lombok.Data;

@Data
public class LoginOtpRequest {

    private String email;
    private String otp;
    private USER_ROLE role;
}
