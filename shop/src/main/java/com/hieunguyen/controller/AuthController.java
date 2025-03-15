package com.hieunguyen.controller;

import com.hieunguyen.dto.request.LoginOtpRequest;
import com.hieunguyen.dto.request.LoginRequest;
import com.hieunguyen.dto.request.SignupRequest;
import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.dto.response.AuthResponse;
import com.hieunguyen.entity.User;
import com.hieunguyen.entity.VerificationCode;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.service.AuthService;
import com.hieunguyen.utils.USER_ROLE;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest request) throws Exception {

        String jwt = authService.createUser(request);

        AuthResponse res = new AuthResponse();

        res.setJwt(jwt);
        res.setMessage("Register success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(res);

    }

    @PostMapping("/sent-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody LoginOtpRequest request) throws Exception {

        authService.sentLoginAndSignupOtp(request.getEmail(), request.getRole());

        ApiResponse res = new ApiResponse();

        res.setMessage("Otp sent successfully");

        return ResponseEntity.ok(res);

    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) throws Exception {

        AuthResponse authResponse = authService.signing(request);

        return ResponseEntity.ok(authResponse);

    }
}
