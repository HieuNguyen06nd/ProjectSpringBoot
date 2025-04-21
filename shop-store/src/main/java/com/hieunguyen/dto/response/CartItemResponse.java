package com.hieunguyen.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private Long id;
    private Long productItemId;
    private String name;
    private String imgUrl;
    private String sku;
    private double price;
    private int quantity;
    private long colorId;
    private long sizeId;
}
