package com.hieunguyen.controller;

import com.hieunguyen.dto.request.SignupRequest;
import com.hieunguyen.dto.response.AuthResponse;
import com.hieunguyen.entity.User;
import com.hieunguyen.service.UserService;
import com.hieunguyen.utils.USER_ROLE;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception {

       User user = userService.findUserByJwtToken(jwt);

        return ResponseEntity.ok(user);

    }
}
