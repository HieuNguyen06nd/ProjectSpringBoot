package com.hieunguyen.service.impl;

import java.util.List;

import com.hieunguyen.dto.CategoryDTO;
import com.hieunguyen.model.CategoryEntity;

public interface ICategoryService {

	CategoryEntity createCategory(CategoryDTO categoryDTO);
	
	CategoryEntity getCategoryById(long id);
	
	List<CategoryEntity> getAllCategory();
	
	CategoryEntity updateCategory(long categoryId, CategoryDTO categoryDTO);
	
	void deleteCategory(long id);
}
