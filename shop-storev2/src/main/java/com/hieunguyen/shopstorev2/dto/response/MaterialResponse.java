package com.hieunguyen.shopstorev2.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialResponse {
    private Long id;
    private String name;
}