package com.hieunguyen.shopstorev2.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Set<String> images;
    private String categoryName;
    private String brandName;
    private List<ProductItemResponse> items;
    private LocalDateTime createdAt;
}