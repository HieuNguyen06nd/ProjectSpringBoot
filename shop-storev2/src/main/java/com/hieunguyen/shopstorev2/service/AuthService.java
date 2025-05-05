package com.hieunguyen.shopstorev2.service;


import com.hieunguyen.shopstorev2.dto.request.LoginRequest;
import com.hieunguyen.shopstorev2.dto.request.RegisterRequest;
import com.hieunguyen.shopstorev2.dto.response.LoginResponse;
import com.hieunguyen.shopstorev2.dto.response.RegisterResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    RegisterResponse register(RegisterRequest request);
}
