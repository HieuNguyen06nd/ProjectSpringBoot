package com.hieunguyen.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Quantity is required")
    private BigDecimal quantity;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;
}