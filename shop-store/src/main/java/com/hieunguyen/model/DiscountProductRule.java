package com.hieunguyen.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "discount_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountProductRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    @Positive
    @Column(name = "min_order_amount")
    private Double minOrderAmount; // Đơn hàng tối thiểu

    @Positive
    @Column(name = "max_discount_amount")
    private Double maxDiscountAmount; // Giảm tối đa

    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Double percentage; // Phần trăm giảm (0-100%)

    @Positive
    @Column(name = "fixed_amount")
    private Double fixedAmount; // Số tiền giảm cố định (nếu có)

    @AssertTrue(message = "Chỉ được nhập phần trăm HOẶC số tiền cố định")
    private boolean isPercentageOrFixedAmountValid() {
        return (percentage == null) != (fixedAmount == null);
    }

    @Transient
    public Double calculateDiscountAmount(Double orderTotal) {
        if (minOrderAmount != null && orderTotal < minOrderAmount) return 0.0;

        Double discount = (percentage != null)
                ? orderTotal * (percentage / 100)
                : fixedAmount;

        return (maxDiscountAmount != null)
                ? Math.min(discount, maxDiscountAmount)
                : discount;
    }
}