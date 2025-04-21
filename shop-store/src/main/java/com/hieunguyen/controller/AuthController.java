package com.hieunguyen.controller;

import com.hieunguyen.dto.request.AuthRequest;
import com.hieunguyen.dto.request.RegisterRequest;
import com.hieunguyen.dto.response.AuthResponse;
import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Đăng ký
    @PostMapping("/register")
    public ResponseData<String> register(@Valid @RequestBody RegisterRequest request) {
        // Nếu có lỗi (RuntimeException) sẽ được GlobalExceptionHandler bắt
        String message = authService.register(request);
        return new ResponseData<>(200, "Đăng ký thành công", message);
    }

    // Xác thực email
    @GetMapping("/verify-email")
    public ResponseData<String> verifyEmail(@RequestParam("token") String token) {
        boolean verified = authService.verifyEmail(token);
        return new ResponseData<>(200, "Xác thực email thành công", "OK");
    }

    // Gửi OTP
    @PostMapping("/send-otp")
    public ResponseData<String> sendOtp(@RequestParam("email") String email) {
        authService.sendOtp(email);
        return new ResponseData<>(200, "OTP đã được gửi đến email: " + email, null);
    }

    // Đăng nhập
    @PostMapping("/login")
    public ResponseData<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request.getEmailOrPhone(), request.getPassword(), request.getOtp());
        return new ResponseData<>(200, "Đăng nhập thành công", new AuthResponse(token));
    }

    // Xác thực OTP
    @PostMapping("/verify-otp")
    public ResponseData<String> verifyOtp(@RequestParam("email") String email,
                                          @RequestParam("otp") String otp) {
        boolean valid = authService.verifyOtp(email, otp);
        return new ResponseData<>(200, "Xác thực OTP thành công", "OK");
    }

    // Đăng xuất
    @PostMapping("/logout")
    public ResponseData<String> logout(@RequestParam String email) {
        authService.logout(email);
        return new ResponseData<>(200, "Đăng xuất thành công", "OK");
    }
}
