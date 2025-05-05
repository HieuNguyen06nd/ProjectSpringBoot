package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.Shop;
import com.hieunguyen.shopstorev2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByOwner(User user);
    boolean existsByOwner(User user);
}