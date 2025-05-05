package com.hieunguyen.shopstorev2.entities;

import com.hieunguyen.shopstorev2.utils.ApplicableType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "discount_usages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountUsage {
    @Id
    @GeneratedValue
    Long id;
    @ManyToOne(fetch=LAZY) @JoinColumn(name="discount_id", nullable=false)
    private Discount discount;

    @ManyToOne(fetch=LAZY) @JoinColumn(name="order_id", nullable=false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private ApplicableType appliedTo;

    @ManyToOne(fetch=LAZY) @JoinColumn(name="product_item_id")
    private ProductItem productItem;

    @Column(name="applied_amount")
    private BigDecimal appliedAmount;

    private LocalDateTime usedAt;

}