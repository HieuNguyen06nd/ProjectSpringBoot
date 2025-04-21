package com.hieunguyen.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotNull(message = "Quantity không được để trống")
    private Integer quantity;
    private Long productItemId;

}
