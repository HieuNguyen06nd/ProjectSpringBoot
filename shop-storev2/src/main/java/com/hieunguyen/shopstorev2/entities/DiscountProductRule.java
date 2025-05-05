package com.hieunguyen.shopstorev2.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "discount_product_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountProductRule {
    @Id
    @GeneratedValue
    Long id;
    @ManyToOne(fetch=LAZY) @JoinColumn(name="discount_id", nullable=false)
    private Discount discount;

    @ManyToOne(fetch=LAZY) @JoinColumn(name="product_item_id", nullable=false)
    private ProductItem productItem;

    @Column(precision=5, scale=2)
    private BigDecimal percentage;

    @Column(name="fixed_amount")
    private BigDecimal fixedAmount;
}