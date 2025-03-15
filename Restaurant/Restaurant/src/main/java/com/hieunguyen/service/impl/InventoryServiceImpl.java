package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.InventoryRequest;
import com.hieunguyen.entity.Inventory;
import com.hieunguyen.entity.Supplier;
import com.hieunguyen.repository.InventoryRepository;
import com.hieunguyen.repository.SupplierRepository;
import com.hieunguyen.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final SupplierRepository supplierRepository;


    @Override
    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    @Override
    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Inventory createInventory(InventoryRequest request) {
        // Tìm Supplier qua supplierId từ DTO
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        Inventory inventory = Inventory.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .supplier(supplier)
                .build();

        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateInventory(Long id, InventoryRequest request) {
        Inventory existing = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        existing.setName(request.getName());
        existing.setQuantity(request.getQuantity());
        existing.setUnit(request.getUnit());
        existing.setSupplier(supplier);

        return inventoryRepository.save(existing);
    }

    @Override
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}
