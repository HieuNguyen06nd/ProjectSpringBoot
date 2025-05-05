package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.SizeRequest;
import com.hieunguyen.shopstorev2.dto.response.SizeResponse;
import com.hieunguyen.shopstorev2.entities.Size;
import com.hieunguyen.shopstorev2.repository.SizeRepository;
import com.hieunguyen.shopstorev2.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;

    @Override
    public SizeResponse create(SizeRequest request) {
        Size size = Size.builder().name(request.getName()).build();
        sizeRepository.save(size);
        return map(size);
    }

    @Override
    public List<SizeResponse> getAll() {
        return sizeRepository.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public SizeResponse update(Long id, SizeRequest request) {
        Size size = sizeRepository.findById(id).orElseThrow();
        size.setName(request.getName());
        sizeRepository.save(size);
        return map(size);
    }

    @Override
    public void delete(Long id) {
        sizeRepository.deleteById(id);
    }

    private SizeResponse map(Size s) {
        return SizeResponse.builder().id(s.getId()).name(s.getName()).build();
    }
}
