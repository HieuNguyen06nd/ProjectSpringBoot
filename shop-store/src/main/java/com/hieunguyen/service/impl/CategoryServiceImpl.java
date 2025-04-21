package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.CategoryRequest;
import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.Category;
import com.hieunguyen.repository.CategoryRepository;
import com.hieunguyen.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .icon(request.getIcon()) // Lưu icon vào category
                .active(request.getActive() != null ? request.getActive() : true) // Xử lý active
                .parent(request.getParentId() != null
                        ? categoryRepository.findById(request.getParentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Danh mục cha không tồn tại"))
                        : null)
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getRootCategories() {
        return categoryRepository.findByParentIsNull();
    }

    @Override
    public List<Category> getSubCategories(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục"));
    }

    @Override
    public Category updateCategory(Long id, CategoryRequest request) {
        Category category = getCategoryById(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIcon(request.getIcon()); // Cập nhật icon
        category.setActive(request.getActive() != null ? request.getActive() : category.getActive()); // Cập nhật active nếu có
        category.setParent(request.getParentId() != null
                ? categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục cha không tồn tại"))
                : null);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    @Override
    public List<Category> getCategoryPath(Long categoryId) {
        List<Category> path = new ArrayList<>();
        Category current = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Đi ngược từ category hiện tại lên root
        while (current != null) {
            path.add(0, current); // Thêm vào đầu để giữ thứ tự root -> current
            current = current.getParent();
        }

        return path;
    }
}
