package com.hieunguyen.shopstorev2.dto.response;

import com.hieunguyen.shopstorev2.utils.ApplicableType;
import com.hieunguyen.shopstorev2.utils.DiscountStatus;
import com.hieunguyen.shopstorev2.utils.DiscountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DiscountResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private ApplicableType applicableTo;
    private DiscountType discountType;
    private BigDecimal percentage;
    private Double fixedAmount;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Integer priority;
    private DiscountStatus status;
    private List<DiscountProductRuleResponse> productRules;
}