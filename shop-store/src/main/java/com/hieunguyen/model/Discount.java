package com.hieunguyen.model;

import com.hieunguyen.utils.ApplicableType;
import com.hieunguyen.utils.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    /** Giá trị giảm: nếu là % thì 0–100, nếu là FIXED thì đơn vị tiền */
    @Column(nullable = false)
    private Double value;

    @Enumerated(EnumType.STRING)
    private DiscountType type;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validTo;

    @Enumerated(EnumType.STRING)
    private ApplicableType applicableTo;

    @ElementCollection
    @CollectionTable(
            name = "discount_product_ids",
            joinColumns = @JoinColumn(name = "discount_id")
    )
    @Column(name = "product_id")
    private List<Long> applicableProductIds;

    private Double minOrderAmount;

    @Column(nullable = false, name = "is_active")
    private Boolean active;

    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


}

