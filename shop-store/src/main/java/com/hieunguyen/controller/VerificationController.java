package com.hieunguyen.controller;

import com.hieunguyen.dto.response.APIResponse;
import com.hieunguyen.model.User;
import com.hieunguyen.reponsitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class VerificationController {

    private final UserRepository userRepository;

    @GetMapping("/verify")
    public ResponseEntity<APIResponse<?>> verifyEmail(@RequestParam("token") String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new APIResponse<>(false, "Token không hợp lệ", null));
        }

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null); // Xóa token sau khi xác nhận
        userRepository.save(user);
        return ResponseEntity.ok(new APIResponse<>(true, "Email đã được xác nhận thành công", null));
    }
}