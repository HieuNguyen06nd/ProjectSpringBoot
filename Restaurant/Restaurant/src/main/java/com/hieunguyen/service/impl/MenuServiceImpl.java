package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.MenuRequest;
import com.hieunguyen.entity.Menu;
import com.hieunguyen.repository.MenuRepository;
import com.hieunguyen.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public Menu createMenu(MenuRequest request) {
        Menu menu = Menu.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
        return menuRepository.save(menu);
    }

    @Override
    public Menu updateMenu(Long id, MenuRequest request) {
        Menu existing = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        return menuRepository.save(existing);
    }

    @Override
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }
}
