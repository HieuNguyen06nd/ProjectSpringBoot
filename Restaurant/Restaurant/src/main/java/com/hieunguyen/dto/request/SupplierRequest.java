package com.hieunguyen.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SupplierRequest {

    @NotBlank(message = "Supplier name is required")
    private String name;

    @NotBlank(message = "Contact info is required")
    private String contactInfo;
}
