package com.hieunguyen.shopstorev2.dto.request;

import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
public class ProductRequest {
    private Long categoryId;
    private Long brandId;
    private String name;
    private String description;
    private Set<String> images;
    private List<ProductItemRequest> items;
}