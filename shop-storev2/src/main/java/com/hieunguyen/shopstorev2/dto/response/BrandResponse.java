package com.hieunguyen.shopstorev2.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandResponse {
    private Long id;
    private String name;
    private String description;
    private String image;
}