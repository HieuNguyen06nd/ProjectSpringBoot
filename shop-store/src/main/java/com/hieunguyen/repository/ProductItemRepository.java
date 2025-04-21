package com.hieunguyen.repository;

import com.hieunguyen.model.ProductItem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    @Query("SELECT pi FROM ProductItem pi " +
            "LEFT JOIN FETCH pi.product p " +
            "LEFT JOIN FETCH pi.color " +
            "LEFT JOIN FETCH pi.size " +
            "LEFT JOIN FETCH pi.material " +
            "WHERE pi.id = :id")
    Optional<ProductItem> findByIdWithDetails(@Param("id") Long id);
}
