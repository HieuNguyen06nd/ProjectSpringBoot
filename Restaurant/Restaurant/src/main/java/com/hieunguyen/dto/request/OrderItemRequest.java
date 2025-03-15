package com.hieunguyen.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderItemRequest {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Menu ID is required")
    private Long menuId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Price is required")
    private BigDecimal price;
}
