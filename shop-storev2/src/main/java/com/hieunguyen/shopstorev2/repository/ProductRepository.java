package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.Product;
import com.hieunguyen.shopstorev2.entities.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByShop(Shop shop);
}