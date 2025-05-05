package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.request.ColorRequest;
import com.hieunguyen.shopstorev2.dto.response.ColorResponse;

import java.util.List;

public interface ColorService {
    ColorResponse create(ColorRequest request);
    List<ColorResponse> getAll();
    ColorResponse update(Long id, ColorRequest request);
    void delete(Long id);
}