package com.hieunguyen.repository;

import com.hieunguyen.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryId (String categoryId);
}
