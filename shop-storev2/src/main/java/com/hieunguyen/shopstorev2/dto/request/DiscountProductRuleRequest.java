package com.hieunguyen.shopstorev2.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DiscountProductRuleRequest {
    @NotNull private Long productItemId;
    private BigDecimal percentage;
    private BigDecimal fixedAmount;
}