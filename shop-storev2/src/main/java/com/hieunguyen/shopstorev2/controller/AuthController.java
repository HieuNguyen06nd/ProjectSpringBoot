package com.hieunguyen.shopstorev2.controller;


import com.hieunguyen.shopstorev2.dto.request.LoginRequest;
import com.hieunguyen.shopstorev2.dto.request.OtpRequest;
import com.hieunguyen.shopstorev2.dto.request.OtpVerifyRequest;
import com.hieunguyen.shopstorev2.dto.request.RegisterRequest;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.LoginResponse;
import com.hieunguyen.shopstorev2.dto.response.RegisterResponse;
import com.hieunguyen.shopstorev2.repository.UserRepository;
import com.hieunguyen.shopstorev2.service.AuthService;
import com.hieunguyen.shopstorev2.service.OtpService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "APIs for managing products")
public class AuthController {

    private final AuthService authService;
    private final JavaMailSender mailSender;
    private final OtpService otpService;
    private final UserRepository userRepository;


    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.success(response);
    }

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ApiResponse.success(response);
    }

    @PostMapping("/send-otp")
    public ApiResponse<String> sendOtp(@RequestBody OtpRequest request) {
        try {
            otpService.sendOtp(request.getEmail());
            return ApiResponse.success("Đã gửi OTP đến email");
        } catch (Exception e) {
            return ApiResponse.error(500, "Lỗi gửi OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ApiResponse<String> verifyOtp(@RequestBody OtpVerifyRequest request) {
        if (otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
                user.setEmailVerified(true);
                userRepository.save(user);
            });
            return ApiResponse.success("Xác thực email thành công");
        }
        return ApiResponse.error(400, "OTP không hợp lệ hoặc đã hết hạn");
    }




}
