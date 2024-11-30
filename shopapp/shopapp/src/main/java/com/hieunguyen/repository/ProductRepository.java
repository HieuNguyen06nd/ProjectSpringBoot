package com.hieunguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hieunguyen.model.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	boolean existsByName(String name);
	
//	Page<ProductEntity> findAll(Pageable pageable); phan trang
}
