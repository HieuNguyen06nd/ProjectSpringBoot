package com.hieunguyen.lakeSide.controller;

import com.hieunguyen.lakeSide.dto.SignupRequest;
import com.hieunguyen.lakeSide.dto.UserDto;
import com.hieunguyen.lakeSide.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    public ResponseEntity<?> signupUser (SignupRequest signupRequest){
        try {
            UserDto createUser = authService.createUser(signupRequest);

            return new ResponseEntity<>(createUser, HttpStatus.OK);
        }catch (EntityActionVetoException)
    }
}
