package com.hieunguyen.shopstorev2.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CartRequest {
    @NotNull
    private List<CartItemRequest> items;
}