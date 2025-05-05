package com.hieunguyen.model;

import com.hieunguyen.utils.ApplicableType;
import com.hieunguyen.utils.DiscountStatus;
import com.hieunguyen.utils.DiscountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discounts")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Discount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicableType applicableTo;  // ORDER hoặc PRODUCT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;    // PERCENTAGE hoặc FIXED_AMOUNT

    @Min(0) @Max(100)
    private Double percentage;            // nếu discountType = PERCENTAGE

    @Positive
    private Double fixedAmount;           // nếu discountType = FIXED_AMOUNT

    // ORDER-level only
    @Positive
    private Double minOrderAmount;

    @Positive
    private Double maxDiscountAmount;

    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    private Integer priority = 1;

    @Enumerated(EnumType.STRING)
    private DiscountStatus status;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscountProductRule> productRules = new ArrayList<>();

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscountUsage> usages = new ArrayList<>();

    /**
     * Tính số tiền giảm áp dụng cho order
     */
    @Transient
    public double calculateForOrder(double orderTotal) {
        if (orderTotal < (minOrderAmount == null ? 0 : minOrderAmount)) return 0;
        double amount = discountType == DiscountType.PERCENTAGE
                ? orderTotal * (percentage / 100)
                : fixedAmount;
        return maxDiscountAmount != null
                ? Math.min(amount, maxDiscountAmount)
                : amount;
    }
}