package com.hieunguyen.controller;

import com.hieunguyen.dto.request.UserUpdateRequest;
import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.dto.response.UserResponse;
import com.hieunguyen.model.User;
import com.hieunguyen.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{userId}")
    public ResponseData<User> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequest request) {
        User updatedUser = userService.updateUser(userId, request);
        return new ResponseData<>(200, "Cập nhật người dùng thành công", updatedUser);
    }

    @GetMapping("/me")
    public ResponseData<UserResponse> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseData<>(401, "Chưa đăng nhập hoặc token không hợp lệ", null);
        }

        String email = authentication.getName();
        UserResponse userResponse = userService.getCurrentUserInfo(email);
        return new ResponseData<>(200, "Lấy thông tin người dùng thành công", userResponse);
    }
}
