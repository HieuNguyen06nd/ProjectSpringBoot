package com.hieunguyen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hieunguyen.model.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserEntityId(Long userId);
}
