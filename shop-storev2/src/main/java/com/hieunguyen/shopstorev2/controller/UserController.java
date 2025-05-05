package com.hieunguyen.shopstorev2.controller;

import com.hieunguyen.shopstorev2.dto.request.ChangePasswordRequest;
import com.hieunguyen.shopstorev2.dto.request.UpdateUserRequest;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.UserResponse;
import com.hieunguyen.shopstorev2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser() {
        return ApiResponse.success(userService.getCurrentUser());
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateCurrentUser(@RequestBody UpdateUserRequest request) {
        return ApiResponse.success(userService.updateCurrentUser(request));
    }

    @PostMapping("/avatar")
    public ApiResponse<UserResponse> uploadAvatar(@RequestParam("file") MultipartFile file) {
        UserResponse user = userService.updateAvatar(file);
        return ApiResponse.success(user);
    }

    @PatchMapping("/change-password")
    public ApiResponse<UserResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        return null;
    }

}

