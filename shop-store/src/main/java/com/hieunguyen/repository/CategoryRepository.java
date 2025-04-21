package com.hieunguyen.repository;

import com.hieunguyen.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNull(); // Tìm danh mục gốc (cha)
    List<Category> findByParentId(Long parentId); // Tìm danh mục con theo cha
}

