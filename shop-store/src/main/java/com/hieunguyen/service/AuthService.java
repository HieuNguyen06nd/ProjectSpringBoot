package com.hieunguyen.service;

import com.hieunguyen.dto.request.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);

    boolean verifyEmail(String token);
    void sendOtp(String email);
    String login(String identifier, String password, String otp);
    boolean verifyOtp(String email, String otp);
    void logout(String email);
    String refreshToken(String refreshToken);
}