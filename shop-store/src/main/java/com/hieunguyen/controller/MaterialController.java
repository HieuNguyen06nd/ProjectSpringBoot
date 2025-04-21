package com.hieunguyen.controller;

import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.exception.ErrorCode;
import com.hieunguyen.model.Material;
import com.hieunguyen.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;

    // Tạo nguyên liệu mới
    @PostMapping
    public ResponseData<Material> createMaterial(@RequestBody Material material) {
        Material newMaterial = materialService.createMaterial(material);
        return new ResponseData<>(200, "Nguyên liệu được tạo thành công", newMaterial);
    }

    // Lấy nguyên liệu theo ID
    @GetMapping("/{id}")
    public ResponseData<Material> getMaterialById(@PathVariable Long id) {
        Material material = materialService.getMaterialById(id);
        if (material == null) {
            return new ResponseData<>(ErrorCode.NOT_FOUND.getCode(), "Nguyên liệu không tồn tại", null);
        }
        return new ResponseData<>(200, "Nguyên liệu được tìm thấy", material);
    }

    // Lấy danh sách tất cả nguyên liệu
    @GetMapping
    public ResponseData<List<Material>> getAllMaterials() {
        List<Material> materials = materialService.getAllMaterials();
        return new ResponseData<>(200, "Danh sách nguyên liệu", materials);
    }

    // Cập nhật nguyên liệu
    @PutMapping("/{id}")
    public ResponseData<Material> updateMaterial(@PathVariable Long id, @RequestBody Material material) {
        Material updatedMaterial = materialService.updateMaterial(id, material);
        if (updatedMaterial == null) {
            return new ResponseData<>(ErrorCode.NOT_FOUND.getCode(), "Nguyên liệu không tồn tại", null);
        }
        return new ResponseData<>(200, "Nguyên liệu được cập nhật thành công", updatedMaterial);
    }

    // Xóa nguyên liệu
    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return new ResponseData<>(200, "Nguyên liệu đã được xóa", null);
    }
}
