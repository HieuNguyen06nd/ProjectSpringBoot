package com.hieunguyen.service.impl;

import com.hieunguyen.config.JwtUtil;
import com.hieunguyen.entity.User;
import com.hieunguyen.enums.StatusUser;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.service.AuthService;
import com.hieunguyen.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    private static final int OTP_EXPIRATION_MINUTES = 5;
    private static final int MAX_OTP_ATTEMPTS = 3;

    @Override
    public void register(String email, String password, String name, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        String token = generateToken();
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setPhone(phone);
        user.setVerificationToken(token);
        user.setStatus(StatusUser.PENDING);
        userRepository.save(user);

        emailService.sendVerificationCode(email, token);
        log.info("User registered successfully: {}", email);
    }

    @Override
    public boolean verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        user.setVerificationToken(null);
        user.setStatus(StatusUser.ACTIVE);
        userRepository.save(user);
        log.info("Email verified successfully for user: {}", user.getEmail());
        return true;
    }

    @Override
    public void sendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime now = LocalDateTime.now();

        // Reset OTP attempts nếu quá 5 phút
        if (user.getLastOtpRequest() == null || now.isAfter(user.getLastOtpRequest().plusMinutes(5))) {
            user.setOtpSentCount(0);
        }

        // Kiểm tra số lần gửi OTP
        if (user.getOtpSentCount() >= 3) {
            throw new RuntimeException("You have reached the maximum OTP requests. Please wait for 5 minutes.");
        }

        // Tạo OTP
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(now.plusMinutes(OTP_EXPIRATION_MINUTES));
        user.setLastOtpRequest(now);
        user.setOtpSentCount(user.getOtpSentCount() + 1);

        userRepository.save(user);

        // Gửi OTP qua email
        emailService.sendOtp(email, otp);
    }


    @Override
    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getOtp() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("OTP has expired or not requested");
        }

        if (!user.getOtp().equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        user.setOtp(null);
        user.setOtpExpiry(null);
        user.resetOtpAttempts();
        userRepository.save(user);

        log.info("OTP verified successfully for user: {}", email);
        return true;
    }


    @Override
    public String login(String identifier, String password, String otp) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));

        User user = userRepository.findByEmailOrPhone(identifier, identifier)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(otp) || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        // Reset OTP sau đăng nhập thành công
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return jwtUtil.generateToken(identifier); // Trả về JWT
    }


    private String generateToken() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(1000000)); // OTP 6 chữ số
    }
}
