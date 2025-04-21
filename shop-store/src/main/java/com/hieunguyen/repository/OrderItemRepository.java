package com.hieunguyen.repository;

import com.hieunguyen.model.OrderItem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.productItem.id = :productItemId")
    Integer getTotalSoldByProductItemId(@Param("productItemId") Long productItemId);

}

