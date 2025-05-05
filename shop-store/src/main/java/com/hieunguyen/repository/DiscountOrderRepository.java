package com.hieunguyen.repository;

import com.hieunguyen.model.DiscountProductRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountOrderRepository extends JpaRepository<DiscountProductRule, Long> {
}
