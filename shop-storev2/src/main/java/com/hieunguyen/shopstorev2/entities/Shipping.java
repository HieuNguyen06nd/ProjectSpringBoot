package com.hieunguyen.shopstorev2.entities;

import com.hieunguyen.shopstorev2.utils.ShippingProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shippings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipping {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShippingProvider provider; // GHN, GHTK, etc.

    private String trackingCode;
    private String status; // delivered, cancelled, returning...

    private BigDecimal shippingFee;
    private String shippingNote;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;

    @Lob
    private String rawResponse; // json từ bên API để debug
}

