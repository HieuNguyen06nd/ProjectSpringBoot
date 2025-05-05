package com.hieunguyen.shopstorev2.dto.request;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String email;
    private String otp;
}
