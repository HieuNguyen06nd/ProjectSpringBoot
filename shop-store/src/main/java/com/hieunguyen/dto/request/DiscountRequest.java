package com.hieunguyen.dto.request;

import com.hieunguyen.utils.ApplicableType;
import com.hieunguyen.utils.DiscountType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DiscountRequest {
    @NotBlank private String code;
    private String name;
    private String description;

    @NotNull private ApplicableType applicableTo;
    @NotNull private DiscountType discountType;
    @Min(0) @Max(100) private Double percentage;
    @Positive private Double fixedAmount;

    // ORDER-level
    @Positive private Double minOrderAmount;
    @Positive private Double maxDiscountAmount;

    @NotNull private LocalDateTime validFrom;
    @NotNull private LocalDateTime validTo;

    private Integer priority = 1;

    // PRODUCT-level
    private List<ProductRuleDTO> productRules;
}