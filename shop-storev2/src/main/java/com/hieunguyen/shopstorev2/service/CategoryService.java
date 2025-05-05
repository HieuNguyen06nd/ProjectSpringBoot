package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.request.CategoryRequest;
import com.hieunguyen.shopstorev2.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);
    List<CategoryResponse> getAll();
    CategoryResponse update(Long id, CategoryRequest request);
    void delete(Long id);
}