package com.hieunguyen.service;

import com.hieunguyen.dto.request.CartItemRequest;
import com.hieunguyen.dto.response.CartResponse;

public interface CartService {
    CartResponse getCartByUserId(Long userId);
    CartResponse addItemToCart(Long userId, CartItemRequest request);
    CartResponse removeItemFromCart(Long userId, Long cartItemId);
    void clearCart(Long userId);
}
