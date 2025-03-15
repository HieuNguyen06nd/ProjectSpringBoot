package com.hieunguyen.identityservice.service.impl;

import com.hieunguyen.identityservice.dto.request.UserCreationRequest;
import com.hieunguyen.identityservice.dto.request.UserUpdateRequest;
import com.hieunguyen.identityservice.dto.response.UserResponse;
import com.hieunguyen.identityservice.entity.User;
import com.hieunguyen.identityservice.enums.Role;
import com.hieunguyen.identityservice.exception.AppException;
import com.hieunguyen.identityservice.exception.ErrorCode;
import com.hieunguyen.identityservice.mapper.UserMapper;
import com.hieunguyen.identityservice.repository.RoleRepository;
import com.hieunguyen.identityservice.repository.UserRepository;
import com.hieunguyen.identityservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request){
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
//        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));


        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }
}
