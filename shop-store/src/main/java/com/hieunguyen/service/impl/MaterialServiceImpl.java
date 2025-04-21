package com.hieunguyen.service.impl;

import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.Material;
import com.hieunguyen.repository.MaterialRepository;
import com.hieunguyen.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Override
    public Material createMaterial(Material material) {
        return materialRepository.save(material);
    }

    @Override
    public Material updateMaterial(Long id, Material material) {
        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));
        existingMaterial.setName(material.getName());
        existingMaterial.setDescription(material.getDescription());
        return materialRepository.save(existingMaterial);
    }

    @Override
    public void deleteMaterial(Long id) {
        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));
        materialRepository.delete(existingMaterial);
    }

    @Override
    public Material getMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));
    }

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }
}
