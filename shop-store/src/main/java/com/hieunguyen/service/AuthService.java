package com.hieunguyen.service;

import com.hieunguyen.config.JwtUtil;
import com.hieunguyen.dto.request.LoginRequest;
import com.hieunguyen.dto.request.RegisterRequest;
import com.hieunguyen.dto.response.APIResponse;
import com.hieunguyen.dto.response.AuthResponse;
import com.hieunguyen.model.User;
import com.hieunguyen.reponsitory.UserRepository;
import com.hieunguyen.utils.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public APIResponse<?> register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new APIResponse<>(false, "Email đã tồn tại", null);
        }

        // Tạo token xác nhận (có thể sử dụng UUID)
        String token = UUID.randomUUID().toString();

        User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.CUSTOMER)
                .emailVerified(false)
                .emailVerificationToken(token)
                .build();

        userRepository.save(newUser);
        emailService.sendEmailVerification(request.getEmail(), token);

        return new APIResponse<>(true, "Đăng ký thành công! Vui lòng kiểm tra email để xác nhận.", null);
    }

    public APIResponse<?> login(LoginRequest request) {
        // Xác thực đăng nhập (nếu không hợp lệ, AuthenticationManager sẽ ném exception)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        if (!user.isEmailVerified()) {
            return new APIResponse<>(false, "Email chưa được xác nhận", null);
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new APIResponse<>(true, "Đăng nhập thành công", new AuthResponse(token));
    }
}