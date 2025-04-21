package com.hieunguyen.controller;

import com.hieunguyen.dto.request.CartItemRequest;
import com.hieunguyen.dto.response.CartResponse;
import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseData<CartResponse> getCart(@PathVariable Long userId) {
        CartResponse cartResponse = cartService.getCartByUserId(userId);
        return new ResponseData<>(HttpStatus.OK.value(), "Cart retrieved successfully", cartResponse);
    }

    @PostMapping("/{userId}/add")
    public ResponseData<CartResponse> addItem(@PathVariable Long userId, @RequestBody CartItemRequest request) {
        CartResponse cartResponse = cartService.addItemToCart(userId, request);
        return new ResponseData<>(HttpStatus.OK.value(), "Item added to cart", cartResponse);
    }

    @DeleteMapping("/{userId}/remove/{itemId}")
    public ResponseData<CartResponse> removeItem(@PathVariable Long userId, @PathVariable Long itemId) {
        CartResponse cartResponse = cartService.removeItemFromCart(userId, itemId);
        return new ResponseData<>(HttpStatus.OK.value(), "Item removed from cart", cartResponse);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseData<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return new ResponseData<>(HttpStatus.OK.value(), "Cart cleared successfully", null);
    }
}
