package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.ChangePasswordRequest;
import com.hieunguyen.shopstorev2.dto.request.UpdateUserRequest;
import com.hieunguyen.shopstorev2.dto.response.UserResponse;
import com.hieunguyen.shopstorev2.entities.User;
import com.hieunguyen.shopstorev2.repository.UserRepository;
import com.hieunguyen.shopstorev2.security.JwtProvider;
import com.hieunguyen.shopstorev2.service.FileStorageService;
import com.hieunguyen.shopstorev2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Override
    public UserResponse getCurrentUser() {
        // Lấy email từ SecurityContext
        String email = getEmailFromSecurityContext();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToUserResponse(user);
    }

    @Override
    public UserResponse updateCurrentUser(UpdateUserRequest requestDto) {
        String email = getEmailFromSecurityContext();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cập nhật các thông tin
        if (requestDto.getUsername() != null) {
            user.setUsername(requestDto.getUsername());
        }
        if (requestDto.getPhone() != null) {
            user.setPhone(requestDto.getPhone());
        }
        if (requestDto.getEmail() != null) {
            user.setEmail(requestDto.getEmail());
        }
        if (requestDto.getAvatarUrl() != null) {
            user.setAvatarUrl(requestDto.getAvatarUrl());
        }

        userRepository.save(user); // lưu lại

        return mapToUserResponse(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest requestDto) {
        String email = getEmailFromSecurityContext();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Mật khẩu cũ không đúng");
        }

        user.setPasswordHash(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserResponse updateAvatar(MultipartFile file) {
        String email = getEmailFromSecurityContext();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String filePath = fileStorageService.saveFile(file, "avatars");
        user.setAvatarUrl(filePath);
        userRepository.save(user);

        return mapToUserResponse(user);
    }

    private String getEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .build();
    }
}
