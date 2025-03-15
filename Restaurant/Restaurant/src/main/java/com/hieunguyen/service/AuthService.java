package com.hieunguyen.service;

public interface AuthService {
    void register(String email, String password, String name, String phone);
    boolean verifyEmail(String token);
    void sendOtp(String email);
    String login(String identifier, String password, String otp);
    boolean verifyOtp(String email, String otp);
}
