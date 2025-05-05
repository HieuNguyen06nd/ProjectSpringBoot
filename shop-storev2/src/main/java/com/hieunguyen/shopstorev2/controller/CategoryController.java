package com.hieunguyen.shopstorev2.controller;

import com.hieunguyen.shopstorev2.dto.request.CategoryRequest;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.CategoryResponse;
import com.hieunguyen.shopstorev2.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> create(@RequestBody @Valid CategoryRequest request) {
        if (request.getParentId() != null && request.getParentId().equals(request.getParentId())) {
            return ApiResponse.error(400, "parentId không hợp lệ");
        }
        return ApiResponse.success(categoryService.create(request));
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAll() {
        return ApiResponse.success(categoryService.getAll());
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> update(@PathVariable Long id, @RequestBody @Valid CategoryRequest request) {
        if (request.getParentId() != null && request.getParentId().equals(id)) {
            return ApiResponse.error(400, "parentId không được trùng id hiện tại");
        }
        return ApiResponse.success(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ApiResponse.success("Xóa danh mục thành công");
    }
}