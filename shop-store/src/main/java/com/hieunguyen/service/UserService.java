package com.hieunguyen.service;

import com.hieunguyen.dto.request.UserUpdateRequest;
import com.hieunguyen.dto.response.UserResponse;
import com.hieunguyen.model.User;

public interface UserService {
    User updateUser(Long userId, UserUpdateRequest request);
    UserResponse getCurrentUserInfo(String email);
}
