package com.hieunguyen.shopstorev2.entities;

import com.hieunguyen.shopstorev2.utils.ApplicableType;
import com.hieunguyen.shopstorev2.utils.DiscountStatus;
import com.hieunguyen.shopstorev2.utils.DiscountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ApplicableType applicableTo;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private DiscountType discountType;

    private BigDecimal percentage;

    @Column(name="fixed_amount")
    private Double fixedAmount;

    @Column(name="min_order_amount")
    private BigDecimal minOrderAmount;

    @Column(name="max_discount_amount")
    private BigDecimal maxDiscountAmount;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    private Integer priority;

    @Enumerated(EnumType.STRING)
    private DiscountStatus status;

    @OneToMany(mappedBy="discount", cascade=ALL, orphanRemoval=true)
    private List<DiscountProductRule> productRules = new ArrayList<>();

    @OneToMany(mappedBy="discount", cascade=ALL, orphanRemoval=true)
    private List<DiscountUsage> usages = new ArrayList<>();

    @Column(name = "max_usage_per_user")
    private Integer maxUsagePerUser;
}
