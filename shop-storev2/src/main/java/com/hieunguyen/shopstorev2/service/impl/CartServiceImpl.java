package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.CartItemRequest;
import com.hieunguyen.shopstorev2.dto.response.CartItemResponse;
import com.hieunguyen.shopstorev2.dto.response.CartResponse;
import com.hieunguyen.shopstorev2.entities.*;
import com.hieunguyen.shopstorev2.repository.*;
import com.hieunguyen.shopstorev2.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductItemRepository productItemRepository;
    private final UserRepository userRepository;

    @Override
    public CartResponse getMyCart() {
        Cart cart = getOrCreateCart();
        return map(cart);
    }

    @Override
    public CartResponse addToCart(CartItemRequest request) {
        Cart cart = getOrCreateCart();
        ProductItem item = productItemRepository.findById(request.getProductItemId()).orElseThrow();

        if (request.getQuantity() > item.getStock()) {
            throw new IllegalArgumentException("Số lượng vượt quá tồn kho");
        }

        CartItem cartItem = cart.getItems().stream()
                .filter(i -> i.getProductItem().getId().equals(item.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            int newQty = cartItem.getQuantity() + request.getQuantity();
            if (newQty > item.getStock()) {
                throw new IllegalArgumentException("Số lượng vượt quá tồn kho");
            }
            cartItem.setQuantity(newQty);
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .productItem(item)
                    .quantity(request.getQuantity())
                    .build();
            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);
        return map(cart);
    }

    @Override
    public CartResponse updateCartItem(Long itemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        return map(item.getCart());
    }

    @Override
    public void removeCartItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Override
    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart() {
        User user = getCurrentUser();
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    private CartResponse map(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream().map(i -> {
            ProductItem p = i.getProductItem();
            return CartItemResponse.builder()
                    .id(i.getId())
                    .productItemId(p.getId())
                    .sku(p.getSku())
                    .productName(p.getProduct().getName())
                    .price(p.getPrice())
                    .quantity(i.getQuantity())
                    .total(p.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                    .build();
        }).toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .items(items)
                .total(total)
                .build();
    }
}
