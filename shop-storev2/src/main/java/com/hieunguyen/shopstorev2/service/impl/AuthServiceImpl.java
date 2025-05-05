package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.LoginRequest;
import com.hieunguyen.shopstorev2.dto.request.RegisterRequest;
import com.hieunguyen.shopstorev2.dto.response.LoginResponse;
import com.hieunguyen.shopstorev2.dto.response.RegisterResponse;
import com.hieunguyen.shopstorev2.entities.User;
import com.hieunguyen.shopstorev2.exception.AppException;
import com.hieunguyen.shopstorev2.exception.ErrorCodes;
import com.hieunguyen.shopstorev2.repository.UserRepository;
import com.hieunguyen.shopstorev2.security.JwtProvider;
import com.hieunguyen.shopstorev2.service.AuthService;
import com.hieunguyen.shopstorev2.utils.AuthProvider;
import com.hieunguyen.shopstorev2.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getInput(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.generateToken(request.getInput());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(jwtProvider.getJwtExpirationMs() / 1000)
                .build();
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        // Check email hoặc phone đã tồn tại chưa
        if (userRepository.findByEmailOrPhone(request.getEmail(), request.getPhone()).isPresent()) {
            throw new AppException(ErrorCodes.USER_ALREADY_EXISTS, "Email hoặc số điện thoại đã được sử dụng");
        }

        // Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Tạo user mới
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(encodedPassword)
                .authProvider(AuthProvider.LOCAL)
                .role(Role.CUSTOMER)
                .emailVerified(false)
                .build();

        userRepository.save(user);

        // Tạo access token
        String token = jwtProvider.generateToken(user.getEmail());

        return RegisterResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtProvider.getJwtExpirationMs() / 1000)
                .build();
    }

}
