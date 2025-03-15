package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.SupplierRequest;
import com.hieunguyen.entity.Supplier;
import com.hieunguyen.repository.SupplierRepository;
import com.hieunguyen.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    public Supplier createSupplier(SupplierRequest request) {
        Supplier supplier = Supplier.builder()
                .name(request.getName())
                .contactInfo(request.getContactInfo())
                .build();
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Long id, SupplierRequest request) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        existing.setName(request.getName());
        existing.setContactInfo(request.getContactInfo());
        return supplierRepository.save(existing);
    }

    @Override
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
