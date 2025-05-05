package com.hieunguyen.shopstorev2.service;

public interface OtpService {
    void sendOtp(String email);
    boolean verifyOtp(String email, String otp);
}
