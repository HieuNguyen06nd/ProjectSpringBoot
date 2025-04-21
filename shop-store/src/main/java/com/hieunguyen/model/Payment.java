package com.hieunguyen.model;

import com.hieunguyen.utils.PaymentMethod;
import com.hieunguyen.utils.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order; // Đơn hàng được thanh toán

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method; // Phương thức thanh toán

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING; // Trạng thái thanh toán

    @Column(nullable = false)
    private double amount; // Số tiền thanh toán

    @Column(length = 255)
    private String transactionId; // Mã giao dịch của cổng thanh toán (nếu có)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

