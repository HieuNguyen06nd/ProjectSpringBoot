package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.request.SizeRequest;
import com.hieunguyen.shopstorev2.dto.response.SizeResponse;

import java.util.List;

public interface SizeService {
    SizeResponse create(SizeRequest request);
    List<SizeResponse> getAll();
    SizeResponse update(Long id, SizeRequest request);
    void delete(Long id);
}