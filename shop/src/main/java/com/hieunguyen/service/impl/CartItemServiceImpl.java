package com.hieunguyen.service.impl;

import com.hieunguyen.entity.Cart;
import com.hieunguyen.entity.CartItem;
import com.hieunguyen.entity.Product;
import com.hieunguyen.entity.User;
import com.hieunguyen.repository.CartItemRepository;
import com.hieunguyen.service.CartItemService;
import com.hieunguyen.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;


    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem item = findCartItemById(id);

        User cartUser = item.getCart().getUser();

        if (cartUser.getId().equals(userId)){
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity()* item.getMrpPrice());
            item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());

            return cartItemRepository.save(item);
        }
        throw new Exception("You can not update this cartItem");
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem cartItem = findCartItemById(cartItemId);

        User cartUser = cartItem.getCart().getUser();
        if (cartUser.getId().equals(userId)){
            cartItemRepository.delete(cartItem);
        }else  throw new Exception("You can not delete this cartItem");

    }

    @Override
    public CartItem findCartItemById(Long id) throws Exception {

        return cartItemRepository.findById(id).orElseThrow(()-> new Exception("Cart item not found with id : "+id));
    }
}
