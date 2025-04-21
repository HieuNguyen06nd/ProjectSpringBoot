package com.hieunguyen.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    @NotNull(message = "Product item ID is required")
    private Long productItemId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
