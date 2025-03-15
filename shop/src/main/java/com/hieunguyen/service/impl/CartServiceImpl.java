package com.hieunguyen.service.impl;

import com.hieunguyen.entity.Cart;
import com.hieunguyen.entity.CartItem;
import com.hieunguyen.entity.Product;
import com.hieunguyen.entity.User;
import com.hieunguyen.repository.CartItemRepository;
import com.hieunguyen.repository.CartRepository;
import com.hieunguyen.service.CartService;
import com.hieunguyen.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository  cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) throws IllegalAccessException {

        Cart cart = findUserCart(user);

        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart,product,size);

        if (isPresent == null){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);

            int totalPrice = quantity* product.getSellingPrice();
            cartItem.setSellingPrice(totalPrice);
            cartItem.setMrpPrice(quantity * product.getMrpPrice());

            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return cartItemRepository.save(cartItem);
        }

        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) throws IllegalAccessException {
        Cart cart = cartRepository.findByUserId(user.getId());

        int  totalPrice = 0;
        int totalDiscountPrice = 0;
        int totalItem = 0;
        for (CartItem cartItem: cart.getCartItems()){
            totalPrice += cartItem.getMrpPrice();
            totalDiscountPrice+= cartItem.getSellingPrice();
            totalItem+= cartItem.getQuantity();
        }
        cart.setTotalItems(totalItem);
        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalSellingPrice(totalDiscountPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountPrice));
        return cart;
    }
    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) throws IllegalAccessException {
        if (mrpPrice <=0){
           return 0;
        }
        double discount = mrpPrice- sellingPrice;
        double discountPercentage = (discount/mrpPrice)*100;

        return (int) discountPercentage;
    }
}
