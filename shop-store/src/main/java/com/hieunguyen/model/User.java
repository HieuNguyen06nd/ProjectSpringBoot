package com.hieunguyen.model;

import com.hieunguyen.utils.Role;
import com.hieunguyen.utils.StatusUser;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    private String phone;

    @Column(nullable = true, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String fullName;

    @Column(unique = true, nullable = true, length = 255)
    private String googleId;

    @Column(unique = true, nullable = true, length = 255)
    private String facebookId;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @Column(length = 255)
    private String emailVerificationToken;

    private LocalDateTime tokenExpiresAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CUSTOMER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusUser status = StatusUser.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Tự động thiết lập createdAt và updatedAt khi lưu mới
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Tự động cập nhật updatedAt khi cập nhật bản ghi
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
