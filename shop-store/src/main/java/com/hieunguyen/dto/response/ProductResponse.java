package com.hieunguyen.dto.response;

import com.hieunguyen.utils.ProductItemStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Category info
    private Long categoryId;
    private String categoryName;

    // Brand info
    private Long brandId;
    private String brandName;

    // Media
    private Set<String> images;

    // Product items với thông tin giảm giá
    private List<ProductItemResponse> productItems;

    @Data
    @Builder
    public static class ProductItemResponse {
        // Basic info
        private Long id;
        private String sku;
        private double price;         // Giá gốc
        private double newPrice;      // Giá sau giảm
        private int stock;
        private ProductItemStatus status;

        // Attributes
        private Long colorId;
        private String colorName;
        private String hexCode;

        private Long sizeId;
        private String sizeName;

        private Long materialId;
        private String materialName;

        // Sales info
        private Integer sold;

        // Discount info (NEW)
        private Double maxDiscountPercent; // % giảm lớn nhất áp dụng
        private List<DiscountInfo> activeDiscounts; // Danh sách discount đang active
    }

    // Inner class cho thông tin discount chi tiết
    @Data
    @Builder
    public static class DiscountInfo {
        private Long discountId;
        private String discountName;
        private String discountType; // "PERCENTAGE" hoặc "FIXED_AMOUNT"
        private Double discountValue;
    }
}