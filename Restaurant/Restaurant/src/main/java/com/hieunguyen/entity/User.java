package com.hieunguyen.entity;

import com.hieunguyen.enums.Role;
import com.hieunguyen.enums.StatusUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STAFF;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(unique = true)
    private String phone;

    private String salary;

    @Column(name = "verification_token")
    private String verificationToken;

    // OTP và xác thực OTP
    private String otp;

    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry; // Thời gian hết hạn OTP

    @Column(name = "otp_attempts")
    private Integer  otpAttempts = 0; // Số lần nhập sai OTP

    @Column(name = "otp_sent_count")
    private Integer otpSentCount = 0; // Số lần gửi OTP trong khoảng thời gian

    @Column(name = "last_otp_sent_time")
    private LocalDateTime lastOtpSentTime; // Thời điểm gửi OTP lần cuối

    @Column(name = "last_otp_request")
    private LocalDateTime lastOtpRequest;

    @Temporal(TemporalType.TIMESTAMP)
    private Date hireDate = new Date();

    @Enumerated(EnumType.STRING)
    private StatusUser status = StatusUser.INACTIVE;

    // Tăng số lần nhập OTP
    public void incrementOtpAttempts() {
        this.otpAttempts++;
    }

    // Reset số lần nhập OTP
    public void resetOtpAttempts() {
        this.otpAttempts = 0;
    }

    // Tăng số lần gửi OTP
    public void incrementOtpSentCount() {
        this.otpSentCount++;
    }

    // Reset số lần gửi OTP
    public void resetOtpSentCount() {
        this.otpSentCount = 0;
    }

    // Cập nhật thời gian gửi OTP lần cuối
    public void updateLastOtpSentTime() {
        this.lastOtpSentTime = LocalDateTime.now();
    }
}
