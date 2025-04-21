package com.hieunguyen.dto.response;

import com.hieunguyen.utils.Role;
import com.hieunguyen.utils.StatusUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String phone;
    private String fullName;
    private boolean emailVerified;
    private Role role;
    private StatusUser status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
