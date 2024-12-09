package com.hieunguyen.shopwq.repository;

import com.hieunguyen.shopwq.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
