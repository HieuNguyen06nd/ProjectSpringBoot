package com.hieunguyen.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMethod; // VNPAY, CASH, etc.
    private Double amount;
    private String transactionId; // Mã giao dịch VNPAY
    private LocalDateTime paymentTime;
    private String status; // SUCCESS, PENDING, FAILED

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Orders order;
}
