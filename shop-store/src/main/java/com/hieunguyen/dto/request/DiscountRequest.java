package com.hieunguyen.dto.request;

import com.hieunguyen.utils.ApplicableType;
import com.hieunguyen.utils.DiscountType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {
    @NotBlank(message = "Discount code is required")
    private String code;

    @NotBlank(message = "Discount name is required")
    private String name;

    private String description;

    @NotNull(message = "Value is required")
    @Positive(message = "Value must be positive")
    private Double value;

    @NotNull(message = "Discount type is required")
    private DiscountType type;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDateTime validFrom;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDateTime validTo;

    @NotNull(message = "Applicable type is required")
    private ApplicableType applicableTo;

    @Size(min = 1, message = "At least one product must be selected", groups = ProductDiscountValidation.class)
    private List<Long> applicableProductIds;

    @NotNull(message = "Minimum order amount is required", groups = OrderDiscountValidation.class)
    @PositiveOrZero(message = "Minimum order amount must be positive or zero")
    private Double minOrderAmount;

    // Validation Groups
    public interface ProductDiscountValidation {}
    public interface OrderDiscountValidation {}
}