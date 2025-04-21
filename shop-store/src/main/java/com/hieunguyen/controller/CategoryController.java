package com.hieunguyen.controller;

import com.hieunguyen.dto.request.CategoryRequest;
import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.model.Category;
import com.hieunguyen.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Tạo danh mục mới
    @PostMapping
    public ResponseData<Category> createCategory(@RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(request);
        return new ResponseData<>(200, "Danh mục được tạo thành công", category);
    }

    // Lấy danh mục theo ID
    @GetMapping("/{id}")
    public ResponseData<Category> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            return new ResponseData<>(404, "Danh mục không tồn tại", null);
        }
        return new ResponseData<>(200, "Danh mục được tìm thấy", category);
    }

    // Lấy danh mục gốc (không có danh mục cha)
    @GetMapping("/root")
    public ResponseData<List<Category>> getRootCategories() {
        List<Category> rootCategories = categoryService.getRootCategories();
        return new ResponseData<>(200, "Danh sách danh mục gốc", rootCategories);
    }

    // Lọc danh mục con theo parentId
    @GetMapping("/sub")
    public ResponseData<List<Category>> getSubCategories(@RequestParam Long parentId) {
        List<Category> subCategories = categoryService.getSubCategories(parentId);
        return new ResponseData<>(200, "Danh sách danh mục con", subCategories);
    }

    // Cập nhật danh mục
    @PutMapping("/{id}")
    public ResponseData<Category> updateCategory(@PathVariable Long id,
                                                 @RequestBody CategoryRequest request) {
        Category updatedCategory = categoryService.updateCategory(id, request);
        if (updatedCategory == null) {
            return new ResponseData<>(404, "Danh mục không tồn tại", null);
        }
        return new ResponseData<>(200, "Danh mục được cập nhật thành công", updatedCategory);
    }

    // Lấy toàn bộ đường dẫn danh mục từ root đến category hiện tại
    @GetMapping("/{id}/path")
    public ResponseData<List<Category>> getCategoryPath(@PathVariable Long id) {
        List<Category> path = categoryService.getCategoryPath(id);
        return new ResponseData<>(200, "Đường dẫn danh mục", path);
    }
    // Xóa danh mục
    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseData<>(200, "Danh mục đã được xóa", null);
    }
}
