package com.hieunguyen.service;

import com.hieunguyen.dto.request.SupplierRequest;
import com.hieunguyen.entity.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierService {

    List<Supplier> getAllSuppliers();
    Optional<Supplier> getSupplierById(Long id);
    Supplier createSupplier(SupplierRequest request);
    Supplier updateSupplier(Long id, SupplierRequest request);
    void deleteSupplier(Long id);
}
