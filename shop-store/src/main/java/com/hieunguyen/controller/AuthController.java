package com.hieunguyen.controller;

import com.hieunguyen.dto.request.LoginRequest;
import com.hieunguyen.dto.request.RegisterRequest;
import com.hieunguyen.dto.response.APIResponse;
import com.hieunguyen.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<?>> register(@RequestBody RegisterRequest request) {
        APIResponse<?> response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<?>> login(@RequestBody LoginRequest request) {
        APIResponse<?> response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}