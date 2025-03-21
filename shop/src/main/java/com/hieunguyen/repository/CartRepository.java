package com.hieunguyen.repository;

import com.hieunguyen.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long id);
}
