package com.hieunguyen.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemResponse> cartItems;
    private int totalQuantity;   // tổng số lượng các item
    private double totalPrice;   // tổng tiền = sum(price * quantity)
}
