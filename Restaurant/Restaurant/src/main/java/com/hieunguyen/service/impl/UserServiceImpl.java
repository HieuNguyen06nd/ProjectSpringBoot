package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.UserCreateRequest;
import com.hieunguyen.entity.User;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(UserCreateRequest userRequest) {
        User user = new User();
        if (existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (existsByPhone(userRequest.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setSalary(userRequest.getSalary());

        if (userRequest.getRole() != null) {
            user.setRole(userRequest.getRole());
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserCreateRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Cập nhật các trường được phép thay đổi cho mọi user
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setPassword(passwordEncoder.encode(
                userRequest.getPassword() != null ? userRequest.getPassword() : user.getPassword()));

        boolean isStaff = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STAFF"));

        // Kiểm tra xem trong request có thay đổi salary hoặc role không
        boolean isSalaryChanged = userRequest.getSalary() != null && !userRequest.getSalary().equals(user.getSalary());
        boolean isRoleChanged = userRequest.getRole() != null && !userRequest.getRole().equals(user.getRole());

        if (isStaff && (isSalaryChanged || isRoleChanged)) {
            log.warn("Staff user [{}] attempted to update salary or role for user id {}.",
                    authentication.getName(), id);
            throw new RuntimeException("Staff users are not allowed to update salary or role");
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }
}
