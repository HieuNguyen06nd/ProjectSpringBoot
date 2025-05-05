package com.hieunguyen.shopstorev2.dto.response;

import com.hieunguyen.shopstorev2.utils.ShopStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopResponse {
    private Long id;
    private String name;
    private String description;
    private String logoUrl;
    private ShopStatus status;
    private boolean deleted;
}