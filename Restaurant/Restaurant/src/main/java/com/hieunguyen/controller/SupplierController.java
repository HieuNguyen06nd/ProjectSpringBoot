package com.hieunguyen.controller;

import com.hieunguyen.dto.request.SupplierRequest;
import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.entity.Supplier;
import com.hieunguyen.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Supplier>>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Supplier>> getSupplierById(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplierById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return ResponseEntity.ok(ApiResponse.success(supplier));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Supplier>> createSupplier(@RequestBody SupplierRequest request) {
        Supplier newSupplier = supplierService.createSupplier(request);
        return ResponseEntity.ok(ApiResponse.success(newSupplier));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Supplier>> updateSupplier(@PathVariable Long id, @RequestBody SupplierRequest request) {
        Supplier updatedSupplier = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedSupplier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier deleted successfully"));
    }
}
