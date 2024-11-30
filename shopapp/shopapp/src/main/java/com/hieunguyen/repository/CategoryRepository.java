package com.hieunguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hieunguyen.dto.CategoryDTO;
import com.hieunguyen.model.CategoryEntity;


@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	CategoryEntity save(CategoryDTO categoryDTO);
	
}
