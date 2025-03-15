package com.hieunguyen.controller;

import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.dto.request.MenuRequest;
import com.hieunguyen.entity.Menu;
import com.hieunguyen.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Menu>>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        return ResponseEntity.ok(ApiResponse.success(menus));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Menu>> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getMenuById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        return ResponseEntity.ok(ApiResponse.success(menu));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Menu>> createMenu(@RequestBody @Valid MenuRequest request) {
        Menu newMenu = menuService.createMenu(request);
        return ResponseEntity.ok(ApiResponse.success(newMenu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Menu>> updateMenu(@PathVariable Long id, @RequestBody @Valid MenuRequest request) {
        Menu updatedMenu = menuService.updateMenu(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedMenu));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.ok(ApiResponse.success("Menu deleted successfully"));
    }
}

