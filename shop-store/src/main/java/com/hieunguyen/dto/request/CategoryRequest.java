package com.hieunguyen.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CategoryRequest {
    private String name;
    private String description;
    private String icon;  // Thêm icon
    private Long parentId;
    private Boolean active = true;
}

