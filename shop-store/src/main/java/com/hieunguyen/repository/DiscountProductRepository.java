package com.hieunguyen.repository;

import com.hieunguyen.model.DiscountUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountProductRepository extends JpaRepository<DiscountUsage, Long> {
}
