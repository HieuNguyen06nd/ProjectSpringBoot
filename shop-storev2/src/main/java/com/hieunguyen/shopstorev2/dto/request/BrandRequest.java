package com.hieunguyen.shopstorev2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BrandRequest {
    @NotBlank
    private String name;
    private String description;
    private String image;
}