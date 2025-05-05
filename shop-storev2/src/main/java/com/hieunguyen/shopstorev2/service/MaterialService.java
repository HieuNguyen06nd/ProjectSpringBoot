package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.request.MaterialRequest;
import com.hieunguyen.shopstorev2.dto.response.MaterialResponse;

import java.util.List;

public interface MaterialService {
    MaterialResponse create(MaterialRequest request);
    List<MaterialResponse> getAll();
    MaterialResponse update(Long id, MaterialRequest request);
    void delete(Long id);
}