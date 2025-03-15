package com.hieunguyen.identityservice.service;

import com.hieunguyen.identityservice.dto.request.UserCreationRequest;
import com.hieunguyen.identityservice.dto.request.UserUpdateRequest;
import com.hieunguyen.identityservice.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreationRequest request);

    UserResponse updateUser(String userId, UserUpdateRequest request);

    void deleteUser(String id);

    UserResponse getUser(String id);

    List<UserResponse> getAllUsers();

}
