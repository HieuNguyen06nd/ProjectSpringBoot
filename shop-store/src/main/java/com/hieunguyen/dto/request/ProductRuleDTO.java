package com.hieunguyen.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductRuleDTO {
    @NotNull
    private Long productItemId;
    @Min(0) @Max(100) private Double percentage;
    @Positive
    private Double fixedAmount;
}