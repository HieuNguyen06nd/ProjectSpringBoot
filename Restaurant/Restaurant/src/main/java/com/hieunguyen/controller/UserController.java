package com.hieunguyen.controller;

import com.hieunguyen.dto.request.UserCreateRequest;
import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.dto.response.UserResponse;
import com.hieunguyen.entity.User;
import com.hieunguyen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponses = users.stream().map(this::mapToUserResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(userResponses));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(ApiResponse.success(mapToUserResponse(user)));
    }


    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserCreateRequest userRequest) {
        User newUser = userService.createUser(userRequest);
        return ResponseEntity.ok(ApiResponse.success(mapToUserResponse(newUser)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @RequestBody UserCreateRequest userRequest) {
        User updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(ApiResponse.success(mapToUserResponse(updatedUser)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getName(),
                user.getRole(),
                user.getEmail(),
                user.getPhone(),
                user.getSalary(),
                user.getHireDate(),
                user.getStatus()
        );
    }
}
