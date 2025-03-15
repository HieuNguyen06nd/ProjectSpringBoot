package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.MenuIngredientRequest;
import com.hieunguyen.entity.Inventory;
import com.hieunguyen.entity.Menu;
import com.hieunguyen.entity.MenuIngredient;
import com.hieunguyen.repository.InventoryRepository;
import com.hieunguyen.repository.MenuIngredientRepository;
import com.hieunguyen.repository.MenuRepository;
import com.hieunguyen.service.MenuIngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuIngredientServiceImpl implements MenuIngredientService {

    private final MenuIngredientRepository menuIngredientRepository;
    private final MenuRepository menuRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public List<MenuIngredient> getAllMenuIngredients() {
        return menuIngredientRepository.findAll();
    }

    @Override
    public Optional<MenuIngredient> getMenuIngredientById(Long id) {
        return menuIngredientRepository.findById(id);
    }

    @Override
    public MenuIngredient createMenuIngredient(MenuIngredientRequest request) {
        // Lấy Menu từ database
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        // Lấy Inventory từ database
        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        MenuIngredient menuIngredient = MenuIngredient.builder()
                .menu(menu)
                .inventory(inventory)
                .quantity(request.getQuantity())
                .build();
        return menuIngredientRepository.save(menuIngredient);
    }

    @Override
    public MenuIngredient updateMenuIngredient(Long id, MenuIngredientRequest request) {
        MenuIngredient existing = menuIngredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuIngredient not found"));

        // Cập nhật lại Menu nếu có thay đổi
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        // Cập nhật lại Inventory nếu có thay đổi
        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        existing.setMenu(menu);
        existing.setInventory(inventory);
        existing.setQuantity(request.getQuantity());

        return menuIngredientRepository.save(existing);
    }

    @Override
    public void deleteMenuIngredient(Long id) {
        menuIngredientRepository.deleteById(id);
    }
}

