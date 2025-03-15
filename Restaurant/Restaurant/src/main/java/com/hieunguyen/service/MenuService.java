package com.hieunguyen.service;

import com.hieunguyen.dto.request.MenuRequest;
import com.hieunguyen.entity.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<Menu> getAllMenus();
    Optional<Menu> getMenuById(Long id);
    Menu createMenu(MenuRequest request);
    Menu updateMenu(Long id, MenuRequest request);
    void deleteMenu(Long id);
}
