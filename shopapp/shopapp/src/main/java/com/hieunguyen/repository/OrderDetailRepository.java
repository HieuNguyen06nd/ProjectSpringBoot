package com.hieunguyen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hieunguyen.model.OrderDetailEntity;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    List<OrderDetailEntity> findByOrderEntityId(Long orderId);  // Correct query

    OrderDetailEntity save(OrderDetailEntity orderDetailEntity);

}

