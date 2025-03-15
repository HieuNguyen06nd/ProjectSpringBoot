package com.hieunguyen.service;


import com.hieunguyen.dto.request.MenuIngredientRequest;
import com.hieunguyen.entity.MenuIngredient;

import java.util.List;
import java.util.Optional;

public interface MenuIngredientService {
    List<MenuIngredient> getAllMenuIngredients();
    Optional<MenuIngredient> getMenuIngredientById(Long id);
    MenuIngredient createMenuIngredient(MenuIngredientRequest request);
    MenuIngredient updateMenuIngredient(Long id, MenuIngredientRequest request);
    void deleteMenuIngredient(Long id);
}
