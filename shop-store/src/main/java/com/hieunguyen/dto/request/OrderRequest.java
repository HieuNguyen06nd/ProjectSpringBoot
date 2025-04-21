package com.hieunguyen.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderRequest {

    @NotNull(message = "UserId không được để trống")
    private Long userId;

    @NotNull(message = "AddressId không được để trống")
    private Long addressId;

    @NotBlank(message = "Tên khách hàng không được để trống")
    private String customerName;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String customerPhone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String customerAddress;

    @NotNull(message = "Danh sách orderItems không được để trống")
    private List<OrderItemRequest> orderItems;

    // Các trường giảm giá (nếu có)
    private Long discountId;
    private Double discountAmount;
}
