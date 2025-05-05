package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    boolean existsBySku(String sku);
}
