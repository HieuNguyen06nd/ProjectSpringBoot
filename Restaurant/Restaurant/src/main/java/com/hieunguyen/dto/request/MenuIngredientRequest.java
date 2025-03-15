package com.hieunguyen.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class MenuIngredientRequest {
    @NotNull(message = "Menu ID is required")
    private Long menuId;

    @NotNull(message = "Inventory ID is required")
    private Long inventoryId;

    @NotNull(message = "Quantity is required")
    private BigDecimal quantity;
}
