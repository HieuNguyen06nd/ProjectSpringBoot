package com.hieunguyen.service.impl;

import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.Color;
import com.hieunguyen.repository.ColorRepository;
import com.hieunguyen.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;

    @Override
    public Color createColor(Color color) {
        return colorRepository.save(color);
    }

    @Override
    public Color updateColor(Long id, Color color) {
        Color existingColor = colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Màu sắc không tồn tại với ID: " + id));
        existingColor.setName(color.getName());
        existingColor.setHexCode(color.getHexCode());
        return colorRepository.save(existingColor);
    }

    @Override
    public void deleteColor(Long id) {
        Color existingColor = colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Màu sắc không tồn tại với ID: " + id));
        colorRepository.delete(existingColor);
    }

    @Override
    public Color getColorById(Long id) {
        return colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Màu sắc không tồn tại với ID: " + id));
    }

    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

}
