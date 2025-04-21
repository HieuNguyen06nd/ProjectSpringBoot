package com.hieunguyen.dto.response;

import com.hieunguyen.utils.ApplicableType;
import com.hieunguyen.utils.DiscountType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DiscountResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Double value;
    private DiscountType type;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private ApplicableType applicableTo;
    private List<Long> applicableProductIds;
    private Double minOrderAmount;
    private Boolean isActive;
    private LocalDateTime createdAt;
}