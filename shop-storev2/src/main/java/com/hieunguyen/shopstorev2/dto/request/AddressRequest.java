package com.hieunguyen.shopstorev2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Chi tiết địa chỉ không được để trống")
    private String detail;

    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    private String city;

    private String district;
    private String ward;
    private Boolean isDefault;
}