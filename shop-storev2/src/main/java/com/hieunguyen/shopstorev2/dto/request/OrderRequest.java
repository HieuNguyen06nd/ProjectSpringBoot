package com.hieunguyen.shopstorev2.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private Long addressId;
    private List<OrderItemRequest> items;
    private String discountCode;
}