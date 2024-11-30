package com.hieunguyen.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hieunguyen.dto.UserDTO;
import com.hieunguyen.dto.UserLoginDTO;
import com.hieunguyen.service.impl.IUserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("${api.prefix}/users")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class UserController {

	IUserService iUserService;
	
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO , BindingResult result) {
		try {
			if(result.hasErrors()) {
				List<String>errorMessages = result.getFieldErrors()
						.stream()
						.map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessages);
			}
			if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
				return ResponseEntity.badRequest().body("password does not match");
			}
			iUserService.createUser(userDTO);
			
			return ResponseEntity.ok("hello post");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
		String token = iUserService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
		
		return ResponseEntity.ok("some token" +token);
		
	}
	

	
	
}
