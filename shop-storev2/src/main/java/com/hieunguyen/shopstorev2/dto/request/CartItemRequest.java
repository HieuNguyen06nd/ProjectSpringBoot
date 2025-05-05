package com.hieunguyen.shopstorev2.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequest {
    @NotNull
    private Long productItemId;

    @NotNull
    @Min(1)
    private Integer quantity;
}