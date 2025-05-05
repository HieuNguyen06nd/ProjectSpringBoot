package com.hieunguyen.dto.response;

import com.hieunguyen.dto.request.ProductRuleDTO;
import com.hieunguyen.utils.ApplicableType;
import com.hieunguyen.utils.DiscountStatus;
import com.hieunguyen.utils.DiscountType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class DiscountResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private ApplicableType applicableTo;
    private DiscountType discountType;
    private Double percentage;
    private Double fixedAmount;
    private Double minOrderAmount;
    private Double maxDiscountAmount;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Integer priority;
    private DiscountStatus status;
    private List<ProductRuleDTO> productRules; // nếu PRODUCT
}