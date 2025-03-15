package com.hieunguyen.service;

import com.hieunguyen.dto.request.InventoryRequest;
import com.hieunguyen.entity.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<Inventory> getAllInventories();
    Optional<Inventory> getInventoryById(Long id);
    Inventory createInventory(InventoryRequest request);
    Inventory updateInventory(Long id, InventoryRequest request);
    void deleteInventory(Long id);
}
