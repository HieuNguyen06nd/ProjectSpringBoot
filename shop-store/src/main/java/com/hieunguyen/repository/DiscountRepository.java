package com.hieunguyen.repository;

import com.hieunguyen.model.Discount;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    boolean existsByCode(String code);

    @Query("SELECT d FROM Discount d WHERE " +
            "d.active  = true AND " +
            "d.validFrom <= :currentDate AND " +
            "d.validTo >= :currentDate AND " +
            "d.applicableTo = 'PRODUCT' AND " +
            ":productId MEMBER OF d.applicableProductIds")
    List<Discount> findActiveProductDiscounts(Long productId, LocalDateTime currentDate);

    @Query("SELECT d FROM Discount d WHERE " +
            "d.active = true AND " +
            "d.validFrom <= :currentDate AND " +
            "d.validTo >= :currentDate AND " +
            "d.applicableTo = 'ORDER'")
    List<Discount> findActiveOrderDiscounts(LocalDateTime currentDate);

    // Tìm các discount ACTIVE áp dụng cho productId cụ thể
    @Query("SELECT d FROM Discount d WHERE " +
            ":productId MEMBER OF d.applicableProductIds AND " +
            "d.active = true AND " +
            "d.validFrom <= CURRENT_TIMESTAMP AND " +
            "d.validTo >= CURRENT_TIMESTAMP")
    List<Discount> findActiveDiscountsByProductId(@Param("productId") Long productId);
}
