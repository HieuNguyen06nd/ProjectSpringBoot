package com.hieunguyen.repository;

import java.awt.print.Pageable;

import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hieunguyen.model.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	boolean existsByName(String name);
	
//	Page<ProductEntity> findAll(Pageable pageable); phan trang
}
