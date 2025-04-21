package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.UserUpdateRequest;
import com.hieunguyen.dto.response.UserResponse;
import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.User;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User updateUser(Long userId, UserUpdateRequest request) {
        // Tìm user theo ID, nếu không có thì ném lỗi
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Kiểm tra dữ liệu đầu vào hợp lệ
        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            user.setFullName(request.getFullName().trim());
        }

        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            user.setPhone(request.getPhone().trim());
        }

        // Lưu user vào DB
        return userRepository.save(user);
    }

    @Override
    public UserResponse getCurrentUserInfo(String email) {
        // Lấy thông tin user từ DB dựa trên email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Map User entity sang DTO
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .fullName(user.getFullName())
                .emailVerified(user.isEmailVerified())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}