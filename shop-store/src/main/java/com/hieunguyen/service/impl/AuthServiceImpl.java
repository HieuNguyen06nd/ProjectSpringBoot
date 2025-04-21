package com.hieunguyen.service.impl;

import com.hieunguyen.config.JwtUtil;
import com.hieunguyen.dto.request.RegisterRequest;
import com.hieunguyen.dto.response.AuthResponse;
import com.hieunguyen.model.User;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.service.AuthService;
import com.hieunguyen.service.EmailService;
import com.hieunguyen.service.OtpService;
import com.hieunguyen.utils.Role;
import com.hieunguyen.utils.StatusUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ott.InvalidOneTimeTokenException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final OtpService otpService;
    private final StringRedisTemplate redisTemplate;

    @Override
    @Transactional
    public String register(RegisterRequest request) {
        // Kiểm tra xem email hoặc số điện thoại đã tồn tại chưa
        Optional<User> existingUser = userRepository.findByEmailOrPhone(request.getEmail(), request.getPhone());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email hoặc số điện thoại đã tồn tại");
        }

        // Tạo mới đối tượng người dùng
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole(Role.CUSTOMER);
        user.setStatus(StatusUser.PENDING); // Chưa kích hoạt
        user.setEmailVerified(false);

        // Tạo token xác minh email và thiết lập thời gian hết hạn (ví dụ: 24h)
        String verificationToken = UUID.randomUUID().toString();
        user.setEmailVerificationToken(verificationToken);
        user.setTokenExpiresAt(LocalDateTime.now().plusHours(24));

        userRepository.save(user);

        // Gửi email xác nhận đến người dùng
        emailService.sendVerificationCode(user.getEmail(), verificationToken);

        return "Đăng ký thành công! Vui lòng kiểm tra email để xác nhận tài khoản.";
    }

    @Override
    public boolean verifyEmail(String token) {
        Optional<User> userOpt = userRepository.findByEmailVerificationToken(token);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Token không hợp lệ");
        }
        User user = userOpt.get();
        if (user.getTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token đã hết hạn");
        }
        user.setEmailVerified(true);
        user.setStatus(StatusUser.ACTIVE);
        user.setEmailVerificationToken(null);
        user.setTokenExpiresAt(null);
        userRepository.save(user);
        return true;
    }

    @Override
    public void sendOtp(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Email không tồn tại");
        }
        // Tạo OTP sử dụng OtpService (OTP sẽ được lưu trong Redis với TTL)
        String otp = otpService.generateOtp(email);
        // Gửi OTP qua email
        emailService.sendOtp(email, otp);
    }

    public String login(String identifier, String password, String otp) {
        // Tìm người dùng bằng email hoặc số điện thoại
        Optional<User> userOpt = userRepository.findByEmailOrPhone(identifier, identifier);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        User user = userOpt.get();
        boolean isAuthenticated = false;

        // Kiểm tra trạng thái xác nhận email của người dùng
        if (!user.isEmailVerified()) {
            // Nếu email chưa xác nhận, chỉ cho phép đăng nhập bằng mật khẩu
            if (password == null || password.isEmpty()) {
                throw new RuntimeException("Email chưa xác nhận. Vui lòng đăng nhập bằng mật khẩu.");
            }
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Mật khẩu không đúng");
            }
            isAuthenticated = true;
        } else {
            // Nếu email đã xác nhận, cho phép đăng nhập bằng mật khẩu hoặc OTP
            if (password != null && !password.isEmpty()) {
                if (!passwordEncoder.matches(password, user.getPassword())) {
                    throw new RuntimeException("Mật khẩu không đúng");
                }
                isAuthenticated = true;
            }

            if (otp != null && !otp.isEmpty()) {
                if (!otpService.verifyOtp(user.getEmail(), otp)) {
                    throw new RuntimeException("OTP không hợp lệ");
                }
                isAuthenticated = true;
            }
        }

        if (!isAuthenticated) {
            throw new RuntimeException("Bạn phải nhập mật khẩu hoặc OTP để đăng nhập.");
        }
        Role role = user.getRole();

        // Tạo JWT token với thông tin email và role
        return jwtUtil.generateToken(user.getEmail(), role);
    }





    @Override
    public String refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token không hợp lệ");
        }

        String email = jwtUtil.extractEmail(refreshToken);
        String storedToken = redisTemplate.opsForValue().get("TOKEN:" + email);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new RuntimeException("Refresh token đã hết hạn hoặc không hợp lệ");
        }

        // Lấy User từ email và lấy Role
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại")); // Kiểm tra nếu không có user thì ném lỗi

        Role role = user.getRole(); // Lấy Role từ User

        // Tạo Access Token mới với email và role
        return jwtUtil.generateToken(email, role);
    }




    @Override
    public boolean verifyOtp(String email, String otp) {
        return otpService.verifyOtp(email, otp);
    }

    @Override
    public void logout(String email) {
        Set<String> keys = redisTemplate.keys("TOKEN:" + email + "*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }
}
