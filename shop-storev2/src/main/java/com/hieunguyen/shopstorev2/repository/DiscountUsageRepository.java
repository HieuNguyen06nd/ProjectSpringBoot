package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.Discount;
import com.hieunguyen.shopstorev2.entities.DiscountUsage;
import com.hieunguyen.shopstorev2.entities.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiscountUsageRepository extends JpaRepository<DiscountUsage, Long> {
    @Query("SELECT COUNT(d) FROM DiscountUsage d WHERE d.discount = :discount AND d.order.user = :user")
    long countByDiscountAndUser(@Param("discount") Discount discount, @Param("user") User user);

}
