package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.CartItemRequest;
import com.hieunguyen.dto.response.CartItemResponse;
import com.hieunguyen.dto.response.CartResponse;
import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.*;
import com.hieunguyen.repository.*;
import com.hieunguyen.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductItemRepository productItemRepository;

    @Override
    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return toCartResponse(cart);
    }

    @Override
    public CartResponse addItemToCart(Long userId, CartItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));

        ProductItem productItem = productItemRepository.findById(request.getProductItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Product item not found"));

        // Kiểm tra nếu item đã tồn tại thì cập nhật số lượng
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProductItem().getId().equals(productItem.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productItem(productItem)
                    .quantity(request.getQuantity())
                    .build();
            cart.addCartItem(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return toCartResponse(savedCart);
    }

    @Override
    public CartResponse removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cart.removeCartItem(item);
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    // ======================== Helper =========================
    private Cart createCartForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = Cart.builder().user(user).build();
        return cartRepository.save(cart);
    }

    private CartResponse toCartResponse(Cart cart) {
        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(ci -> {
                    Product product = ci.getProductItem().getProduct();
                    // Lấy ảnh đầu tiên trong set images của Product
                    String imgUrl = product.getImages().stream()
                            .findFirst()
                            .orElse(""); // hoặc một URL mặc định nếu trống
                    return CartItemResponse.builder()
                            .id(ci.getId())
                            .productItemId(ci.getProductItem().getId())
                            .name(product.getName())
                            .imgUrl(imgUrl)
                            .sku(ci.getProductItem().getSku())
                            .price(ci.getProductItem().getPrice())
                            .quantity(ci.getQuantity())
                            .colorId(ci.getProductItem().getColor().getId())
                            .colorName(ci.getProductItem().getColor().getName())
                            .sizeId(ci.getProductItem().getSize().getId())
                            .sizeName(ci.getProductItem().getSize().getName())
                            .build();
                })
                .collect(Collectors.toList());

        int totalQty = items.stream().mapToInt(CartItemResponse::getQuantity).sum();
        double totalPrice = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .cartItems(items)
                .totalQuantity(totalQty)
                .totalPrice(totalPrice)
                .build();
    }

}
