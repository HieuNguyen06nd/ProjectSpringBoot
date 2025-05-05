package com.hieunguyen.shopstorev2.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long productItemId;
    private Integer quantity;
}