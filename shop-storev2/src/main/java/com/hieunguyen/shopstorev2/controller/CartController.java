// CartController.java
package com.hieunguyen.shopstorev2.controller;

import com.hieunguyen.shopstorev2.dto.request.CartItemRequest;
import com.hieunguyen.shopstorev2.dto.request.CartRequest;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.CartResponse;
import com.hieunguyen.shopstorev2.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ApiResponse<CartResponse> getMyCart() {
        return ApiResponse.success(cartService.getMyCart());
    }

    @PostMapping("/add")
    public ApiResponse<CartResponse> addToCart(@RequestBody @Valid CartItemRequest request) {
        return ApiResponse.success(cartService.addToCart(request));
    }

    @PostMapping("/bulk")
    public ApiResponse<CartResponse> addMany(@RequestBody @Valid CartRequest request) {
        CartResponse last = null;
        for (CartItemRequest item : request.getItems()) {
            last = cartService.addToCart(item);
        }
        return ApiResponse.success(last);
    }

    @PatchMapping("/item/{itemId}")
    public ApiResponse<CartResponse> updateQuantity(
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        return ApiResponse.success(cartService.updateCartItem(itemId, quantity));
    }

    @DeleteMapping("/item/{itemId}")
    public ApiResponse<String> removeItem(@PathVariable Long itemId) {
        cartService.removeCartItem(itemId);
        return ApiResponse.success("Xoá thành công");
    }

    @DeleteMapping("/clear")
    public ApiResponse<String> clearCart() {
        cartService.clearCart();
        return ApiResponse.success("Đã xoá toàn bộ giỏ hàng");
    }
}
