package com.hieunguyen.lakeSide.controller;

import com.hieunguyen.lakeSide.dto.request.AuthenticationRequest;
import com.hieunguyen.lakeSide.dto.response.AuthenticationResponse;
import com.hieunguyen.lakeSide.dto.request.SignupRequest;
import com.hieunguyen.lakeSide.dto.UserDto;
import com.hieunguyen.lakeSide.model.User;
import com.hieunguyen.lakeSide.repository.UserRepository;
import com.hieunguyen.lakeSide.service.iml.IAuthService;
import com.hieunguyen.lakeSide.service.iml.IUserService;
import com.hieunguyen.lakeSide.util.JwtUtil;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final IUserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        try {
            UserDto createUser = authService.createUser(signupRequest);
            return new ResponseEntity<>(createUser, HttpStatus.CREATED);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>("User not created, please try again later", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest request) {
        try {
            // Authenticate the user with username and password
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        } catch (BadCredentialsException e) {
            // Return an unauthorized response if credentials are incorrect
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Changed to HttpStatus.UNAUTHORIZED (401)
        }

        // Load user details and generate JWT token
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(request.getEmail());
        Optional<User> optionalUser = userRepository.findFistByEmail(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            final String jwt = jwtUtil.generateToken(userDetails);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserRole(String.valueOf(optionalUser.get().getUserRole()));
            authenticationResponse.setUserId(String.valueOf(optionalUser.get().getId()));
            return ResponseEntity.ok(authenticationResponse); // Return the response with a JWT token
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // If user not found, return 404
        }
    }
}
