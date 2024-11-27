package com.hieunguyen.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hieunguyen.dto.CategoryDTO;
import com.hieunguyen.model.CategoryEntity;
import com.hieunguyen.repository.CategoryRepository;
import com.hieunguyen.service.impl.ICategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor 
public class CategoryService implements ICategoryService{

	CategoryRepository categoryRepository;
	
	@Override
	public CategoryEntity createCategory(CategoryDTO categoryDTO) {

		CategoryEntity categoryEntity = CategoryEntity.builder()
				.name(categoryDTO.getName())
				.build();
		return categoryRepository.save(categoryEntity);
	}

	@Override
	public CategoryEntity getCategoryById(long id) {
		// TODO Auto-generated method stub
		return categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("cate not found"));
	}

	@Override
	public List<CategoryEntity> getAllCategory() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}

	@Override
	public CategoryEntity updateCategory(long categoryId, CategoryDTO categoryDTO) {
		CategoryEntity exitsingCategory = getCategoryById(categoryId);
		
		exitsingCategory.setName(categoryDTO.getName());
		
		categoryRepository.save(exitsingCategory);
		return exitsingCategory;
	}

	@Override
	public void deleteCategory(long id) {
		// xoa cung
		categoryRepository.deleteById(id);
	}

}
