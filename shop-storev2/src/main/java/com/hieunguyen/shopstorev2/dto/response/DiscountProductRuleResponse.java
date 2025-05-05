package com.hieunguyen.shopstorev2.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DiscountProductRuleResponse {
    private Long id;
    private Long productItemId;
    private BigDecimal percentage;
    private BigDecimal fixedAmount;
}