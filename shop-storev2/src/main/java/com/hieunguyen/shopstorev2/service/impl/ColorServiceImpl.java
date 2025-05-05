package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.ColorRequest;
import com.hieunguyen.shopstorev2.dto.response.ColorResponse;
import com.hieunguyen.shopstorev2.entities.Color;
import com.hieunguyen.shopstorev2.repository.ColorRepository;
import com.hieunguyen.shopstorev2.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;

    @Override
    public ColorResponse create(ColorRequest request) {
        Color color = Color.builder()
                .name(request.getName())
                .hexColor(request.getHexColor())
                .build();
        colorRepository.save(color);
        return mapToDto(color);
    }

    @Override
    public List<ColorResponse> getAll() {
        return colorRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ColorResponse update(Long id, ColorRequest request) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found"));

        color.setName(request.getName());
        color.setHexColor(request.getHexColor());
        colorRepository.save(color);
        return mapToDto(color);
    }

    @Override
    public void delete(Long id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found"));
        colorRepository.delete(color);
    }

    private ColorResponse mapToDto(Color color) {
        return ColorResponse.builder()
                .id(color.getId())
                .name(color.getName())
                .hexColor(color.getHexColor())
                .build();
    }
}