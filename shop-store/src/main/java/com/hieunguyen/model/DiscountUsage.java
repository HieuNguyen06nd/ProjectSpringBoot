package com.hieunguyen.model;

import com.hieunguyen.utils.ApplicableType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "discount_usages")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DiscountUsage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Enumerated(EnumType.STRING)
    private ApplicableType appliedTo;  // ORDER hoặc PRODUCT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_id")
    private ProductItem productItem;   // null nếu appliedTo = ORDER

    @Column(nullable = false)
    private Double appliedAmount;
}