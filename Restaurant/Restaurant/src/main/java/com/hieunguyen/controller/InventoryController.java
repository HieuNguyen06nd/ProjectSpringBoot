package com.hieunguyen.controller;

import com.hieunguyen.dto.request.InventoryRequest;
import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.entity.Inventory;
import com.hieunguyen.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Inventory>>> getAllInventories() {
        List<Inventory> inventories = inventoryService.getAllInventories();
        return ResponseEntity.ok(ApiResponse.success(inventories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Inventory>> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        return ResponseEntity.ok(ApiResponse.success(inventory));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Inventory>> createInventory(@RequestBody @Valid InventoryRequest request) {
        Inventory newInventory = inventoryService.createInventory(request);
        return ResponseEntity.ok(ApiResponse.success(newInventory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Inventory>> updateInventory(@PathVariable Long id,
                                                                  @RequestBody @Valid InventoryRequest request) {
        Inventory updatedInventory = inventoryService.updateInventory(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedInventory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory deleted successfully"));
    }
}

