package com.hieunguyen.identityservice.controller;

import com.hieunguyen.identityservice.dto.request.ApiResponse;
import com.hieunguyen.identityservice.dto.request.UserCreationRequest;
import com.hieunguyen.identityservice.dto.request.UserUpdateRequest;
import com.hieunguyen.identityservice.dto.response.UserResponse;
import com.hieunguyen.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.createUser(request));

        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId){
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return "User has been deleted";
    }
}
