package com.hieunguyen.shopstorev2.dto.request;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String description;
    private String imageUrl;
    private Long parentId;
}