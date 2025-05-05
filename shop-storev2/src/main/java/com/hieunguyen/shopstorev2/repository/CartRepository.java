package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.Cart;
import com.hieunguyen.shopstorev2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
