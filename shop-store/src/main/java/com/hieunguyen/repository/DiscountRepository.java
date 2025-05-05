package com.hieunguyen.repository;

import com.hieunguyen.model.Discount;
import com.hieunguyen.utils.ApplicableType;
import com.hieunguyen.utils.DiscountStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByCodeAndApplicableTo(String code, ApplicableType type);
}
