package com.hieunguyen.service;

import com.hieunguyen.dto.request.UserCreateRequest;
import com.hieunguyen.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(UserCreateRequest userRequest);
    User updateUser(Long id, UserCreateRequest userRequest);
    void deleteUser(Long id);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
