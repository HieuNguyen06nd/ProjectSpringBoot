package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.CategoryRequest;
import com.hieunguyen.shopstorev2.dto.response.CategoryResponse;
import com.hieunguyen.shopstorev2.entities.Category;
import com.hieunguyen.shopstorev2.repository.CategoryRepository;
import com.hieunguyen.shopstorev2.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        Category parent = request.getParentId() != null
                ? categoryRepository.findById(request.getParentId()).orElse(null)
                : null;

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .parent(parent)
                .build();

        categoryRepository.save(category);
        return map(category);
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow();
        Category parent = request.getParentId() != null
                ? categoryRepository.findById(request.getParentId()).orElse(null)
                : null;

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setParent(parent);

        categoryRepository.save(category);
        return map(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryResponse map(Category c) {
        return CategoryResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .imageUrl(c.getImageUrl())
                .parentId(c.getParent() != null ? c.getParent().getId() : null)
                .parentName(c.getParent() != null ? c.getParent().getName() : null)
                .build();
    }
}