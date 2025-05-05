package com.hieunguyen.shopstorev2.dto.response;

import com.hieunguyen.shopstorev2.utils.ProductItemStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductItemResponse {
    private Long id;
    private BigDecimal price;
    private BigDecimal discountedPrice; // ✅ thêm vào đây
    private String sku;
    private Integer stock;
    private String colorName;
    private String sizeName;
    private String materialName;
    private ProductItemStatus status;
}