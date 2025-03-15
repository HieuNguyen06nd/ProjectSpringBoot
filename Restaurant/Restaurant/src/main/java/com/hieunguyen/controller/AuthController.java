package com.hieunguyen.controller;

import com.hieunguyen.config.JwtUtil;
import com.hieunguyen.dto.request.EmailRequest;
import com.hieunguyen.dto.request.LoginRequest;
import com.hieunguyen.dto.request.UserCreateRequest;
import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    /**
     * Đăng ký tài khoản
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody UserCreateRequest userRequest) {
        log.info("Received registration request for email: {}", userRequest.getEmail());
        try {
            authService.register(userRequest.getEmail(), userRequest.getPassword(), userRequest.getName(), userRequest.getPhone());
            return ResponseEntity.ok(ApiResponse.success("Registration successful. Please check your email to verify your account."));
        } catch (RuntimeException e) {
            log.error("Registration error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "An unexpected error occurred."));
        }
    }

    /**
     * Xác minh email bằng token
     */
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token) {
        log.info("Verifying email with token: {}", token);
        if (authService.verifyEmail(token)) {
            return ResponseEntity.ok(ApiResponse.success("Email verified successfully!"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "Invalid or expired token"));
    }

    /**
     * Đăng nhập bằng email/phone + password + OTP
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request for: {}", loginRequest.getIdentifier());
        try {
            String token = authService.login(loginRequest.getIdentifier(), loginRequest.getPassword(), loginRequest.getOtp());
            return ResponseEntity.ok(ApiResponse.success(token));
        } catch (RuntimeException e) {
            log.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(401, e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "An unexpected error occurred."));
        }
    }

    /**
     * Gửi OTP đến email để xác thực đăng nhập
     */
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(@RequestBody EmailRequest emailRequest) {
        log.info("Request to send OTP for email: {}", emailRequest.getEmail());
        try {
            authService.sendOtp(emailRequest.getEmail());
            return ResponseEntity.ok(ApiResponse.success("OTP has been sent to your email."));
        } catch (RuntimeException e) {
            log.error("OTP sending error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error while sending OTP: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "An unexpected error occurred."));
        }
    }

}
