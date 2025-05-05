package com.hieunguyen.shopstorev2.dto.request;

import com.hieunguyen.shopstorev2.utils.ProductItemStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductItemRequest {
    private Long colorId;
    private Long sizeId;
    private Long materialId;
    private String sku;
    private BigDecimal price;
    private Integer stock;
    private ProductItemStatus status;
}