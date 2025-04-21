package com.hieunguyen.service;

import com.hieunguyen.model.Material;
import java.util.List;

public interface MaterialService {
    Material createMaterial(Material material);
    Material updateMaterial(Long id, Material material);
    void deleteMaterial(Long id);
    Material getMaterialById(Long id);
    List<Material> getAllMaterials();
}
