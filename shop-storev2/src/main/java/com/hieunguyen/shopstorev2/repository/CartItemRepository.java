package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}