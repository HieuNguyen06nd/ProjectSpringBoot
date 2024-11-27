package com.hieunguyen.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hieunguyen.dto.CategoryDTO;
import com.hieunguyen.model.CategoryEntity;
import com.hieunguyen.service.CategoryService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("${api.prefix}/categories")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
//@Validated
public class CategoryController {
	
	CategoryService categoryService;
	
	@GetMapping("")
	public  ResponseEntity<List<CategoryEntity>> getAllCategory(
			@RequestParam("page") int page,
			@RequestParam("limit") int limit
			) {
		List<CategoryEntity> categoryEntities = categoryService.getAllCategory();
		return ResponseEntity.ok(categoryEntities);
	}
	
	
	@PostMapping("")
	public  ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
			BindingResult result) {
		if(result.hasErrors()) {
			List<String>errorMessages = result.getFieldErrors()
					.stream()
					.map(FieldError::getDefaultMessage)
					.toList();
			return ResponseEntity.badRequest().body(errorMessages);
		}
		categoryService.createCategory(categoryDTO);
		return ResponseEntity.ok("Create Category successfully"+categoryDTO);
	}
	
	@PutMapping("/{id}")
	public  ResponseEntity<String> updateCategory(@Valid
			@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
		categoryService.updateCategory(id, categoryDTO);
		return ResponseEntity.ok("Update Category successfully");
	}
	
	@DeleteMapping("/{id}")
	public  ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.ok("Delete Category successfully" + id);
	}
	
	
}
