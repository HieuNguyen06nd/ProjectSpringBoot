package com.hieunguyen.shopstorev2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShopRequest {
    @NotBlank
    private String name;
    private String description;
    private String logoUrl; // upload sau cũng được
}