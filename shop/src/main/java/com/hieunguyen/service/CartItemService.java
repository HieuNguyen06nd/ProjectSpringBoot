package com.hieunguyen.service;

import com.hieunguyen.entity.CartItem;

public interface CartItemService {

    CartItem updateCartItem (Long userId, Long id, CartItem cartItem) throws Exception;
    void  deleteCartItem(Long userId, Long cartItemId) throws Exception;
    CartItem findCartItemById(Long id) throws Exception;
    
}
