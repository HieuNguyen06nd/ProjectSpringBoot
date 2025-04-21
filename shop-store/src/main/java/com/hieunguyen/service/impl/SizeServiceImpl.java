package com.hieunguyen.service.impl;

import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.Size;
import com.hieunguyen.repository.SizeRepository;
import com.hieunguyen.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;

    @Override
    public Size createSize(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    public Size updateSize(Long id, Size size) {
        Size existingSize = sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Size not found with id: " + id));
        existingSize.setName(size.getName());
        return sizeRepository.save(existingSize);
    }

    @Override
    public void deleteSize(Long id) {
        Size existingSize = sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Size not found with id: " + id));
        sizeRepository.delete(existingSize);
    }

    @Override
    public Size getSizeById(Long id) {
        return sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Size not found with id: " + id));
    }

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }
}
