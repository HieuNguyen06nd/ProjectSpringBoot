package com.hieunguyen.service;

public interface EmailService {
    void sendVerificationCode(String to, String code);
    void sendOtp(String to, String otp);
}
