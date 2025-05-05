package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.Discount;
import com.hieunguyen.shopstorev2.entities.DiscountProductRule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByCode(String code);

    @Query("""
    SELECT r FROM DiscountProductRule r
    JOIN r.discount d
    WHERE r.productItem.id = :itemId
    AND d.status = 'ACTIVE'
    AND (d.validFrom IS NULL OR d.validFrom <= :now)
    AND (d.validTo IS NULL OR d.validTo >= :now)
""")
    List<DiscountProductRule> findActiveRulesByProductItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

}
