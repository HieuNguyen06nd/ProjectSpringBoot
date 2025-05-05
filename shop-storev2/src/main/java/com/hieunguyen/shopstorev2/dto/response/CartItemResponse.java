package com.hieunguyen.shopstorev2.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {
    private Long id;
    private Long productItemId;
    private String sku;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal total;
}