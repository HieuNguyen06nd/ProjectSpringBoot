package com.hieunguyen.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Long userId;
    private Long addressId; // Đảm bảo có trường này
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String status;
    private double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long discountId;
    private Double discountAmount;
    private List<OrderItemResponse> orderItems;

    @Data
    @Builder
    public static class OrderItemResponse {
        private Long id;
        private Long productItemId;
        private Long productId;
        private String productName;
        private Long colorId;
        private String colorName;
        private Long sizeId;
        private String sizeName;
        private Long materialId;
        private String materialName;
        private String sku;
        private Integer quantity;
        private Double price;
    }

}

