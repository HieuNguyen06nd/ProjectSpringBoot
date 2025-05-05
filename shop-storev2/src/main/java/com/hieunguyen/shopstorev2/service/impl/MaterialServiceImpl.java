package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.MaterialRequest;
import com.hieunguyen.shopstorev2.dto.response.MaterialResponse;
import com.hieunguyen.shopstorev2.entities.Material;
import com.hieunguyen.shopstorev2.repository.MaterialRepository;
import com.hieunguyen.shopstorev2.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Override
    public MaterialResponse create(MaterialRequest request) {
        Material material = Material.builder().name(request.getName()).build();
        materialRepository.save(material);
        return map(material);
    }

    @Override
    public List<MaterialResponse> getAll() {
        return materialRepository.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public MaterialResponse update(Long id, MaterialRequest request) {
        Material material = materialRepository.findById(id).orElseThrow();
        material.setName(request.getName());
        materialRepository.save(material);
        return map(material);
    }

    @Override
    public void delete(Long id) {
        materialRepository.deleteById(id);
    }

    private MaterialResponse map(Material m) {
        return MaterialResponse.builder().id(m.getId()).name(m.getName()).build();
    }
}