package com.hieunguyen.controller;

import com.hieunguyen.dto.request.MenuIngredientRequest;
import com.hieunguyen.entity.MenuIngredient;
import com.hieunguyen.service.MenuIngredientService;
import com.hieunguyen.dto.response.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/menu-ingredients")
public class MenuIngredientController {

    private final MenuIngredientService menuIngredientService;

    public MenuIngredientController(MenuIngredientService menuIngredientService) {
        this.menuIngredientService = menuIngredientService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuIngredient>>> getAllMenuIngredients() {
        List<MenuIngredient> menuIngredients = menuIngredientService.getAllMenuIngredients();
        return ResponseEntity.ok(ApiResponse.success(menuIngredients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuIngredient>> getMenuIngredientById(@PathVariable Long id) {
        MenuIngredient menuIngredient = menuIngredientService.getMenuIngredientById(id)
                .orElseThrow(() -> new RuntimeException("MenuIngredient not found"));
        return ResponseEntity.ok(ApiResponse.success(menuIngredient));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'STAFF')")
    @PostMapping
    public ResponseEntity<ApiResponse<MenuIngredient>> createMenuIngredient(@Valid @RequestBody MenuIngredientRequest request) {
        MenuIngredient menuIngredient = menuIngredientService.createMenuIngredient(request);
        return ResponseEntity.ok(ApiResponse.success(menuIngredient));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuIngredient>> updateMenuIngredient(
            @PathVariable Long id,
            @Valid @RequestBody MenuIngredientRequest request) {
        MenuIngredient updated = menuIngredientService.updateMenuIngredient(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMenuIngredient(@PathVariable Long id) {
        menuIngredientService.deleteMenuIngredient(id);
        return ResponseEntity.ok(ApiResponse.success("MenuIngredient deleted successfully"));
    }

}
