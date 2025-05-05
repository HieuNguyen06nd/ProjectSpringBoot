package com.hieunguyen.shopstorev2.dto.request;

import com.hieunguyen.shopstorev2.utils.ApplicableType;
import com.hieunguyen.shopstorev2.utils.DiscountStatus;
import com.hieunguyen.shopstorev2.utils.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DiscountRequest {
    @NotBlank private String code;
    @NotBlank private String name;
    private String description;
    @NotNull private ApplicableType applicableTo;
    @NotNull private DiscountType discountType;
    private BigDecimal percentage;
    private Double fixedAmount;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Integer priority;
    private DiscountStatus status;
    private List<DiscountProductRuleRequest> productRules;
}