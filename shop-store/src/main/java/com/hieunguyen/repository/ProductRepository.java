package com.hieunguyen.repository;

import com.hieunguyen.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
    SELECT oi.productItem.product, SUM(oi.quantity) AS totalSold
    FROM OrderItem oi
    GROUP BY oi.productItem.product
    ORDER BY totalSold DESC
""")
    List<Object[]> findTopSellingProducts(Pageable pageable);


}
