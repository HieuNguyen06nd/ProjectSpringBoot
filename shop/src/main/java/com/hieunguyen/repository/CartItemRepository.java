package com.hieunguyen.repository;

import com.hieunguyen.entity.Cart;
import com.hieunguyen.entity.CartItem;
import com.hieunguyen.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
