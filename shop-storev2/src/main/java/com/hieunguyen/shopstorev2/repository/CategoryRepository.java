package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
