package com.hieunguyen.service;

import com.hieunguyen.dto.request.CategoryRequest;
import com.hieunguyen.model.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(CategoryRequest request);

    List<Category> getRootCategories(); // Lấy danh mục gốc (cha)

    List<Category> getSubCategories(Long parentId); // Lấy danh mục con theo danh mục cha

    Category getCategoryById(Long id);

    Category updateCategory(Long id, CategoryRequest request);

    List<Category> getCategoryPath(Long categoryId);

    void deleteCategory(Long id);
}

