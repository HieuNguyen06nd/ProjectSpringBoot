package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.request.CartItemRequest;
import com.hieunguyen.shopstorev2.dto.request.CartRequest;
import com.hieunguyen.shopstorev2.dto.response.CartResponse;

public interface CartService {
    CartResponse getMyCart();
    CartResponse addToCart(CartItemRequest request);
    CartResponse updateCartItem(Long itemId, Integer quantity);
    void removeCartItem(Long itemId);
    void clearCart();
}