package com.hieunguyen.dto.request;

import com.hieunguyen.utils.ProductItemStatus;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductItemRequest {

    private Long id;

    @NotNull(message = "Màu sắc không được để trống")
    private Long colorId;

    private String colorName;  // Nếu không có ID, dùng để tạo mới

    private String hexCode;    // Mã màu cho Color mới

    @NotNull(message = "Kích thước không được để trống")
    private Long sizeId;

    private String sizeName;   // Nếu không có ID, dùng để tạo mới

    @NotNull(message = "Chất liệu không được để trống")
    private Long materialId;

    private String materialName;

    @NotNull(message = "Giá sản phẩm không được để trống")
    private double price;

    private int stock;

    private boolean isActive;

    private String sku;

    private ProductItemStatus status;
}
