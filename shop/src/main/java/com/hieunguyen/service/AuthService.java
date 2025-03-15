package com.hieunguyen.service;

import com.hieunguyen.dto.request.LoginRequest;
import com.hieunguyen.dto.request.SignupRequest;
import com.hieunguyen.dto.response.AuthResponse;
import com.hieunguyen.utils.USER_ROLE;

public interface AuthService {

    void sentLoginAndSignupOtp(String email, USER_ROLE role) throws Exception;

    String createUser(SignupRequest request) throws Exception;

    AuthResponse signing(LoginRequest request);
}
