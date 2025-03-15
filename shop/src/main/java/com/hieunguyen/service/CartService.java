package com.hieunguyen.service;

import com.hieunguyen.entity.Cart;
import com.hieunguyen.entity.CartItem;
import com.hieunguyen.entity.Product;
import com.hieunguyen.entity.User;

public interface CartService {
    CartItem addCartItem(
            User user,
            Product product,
            String size,
            int quantity
    ) throws IllegalAccessException;
    Cart findUserCart(User user) throws IllegalAccessException;
}
