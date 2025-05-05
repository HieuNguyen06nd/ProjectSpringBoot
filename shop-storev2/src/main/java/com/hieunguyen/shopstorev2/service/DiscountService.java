package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.request.DiscountRequest;
import com.hieunguyen.shopstorev2.dto.response.DiscountResponse;

import java.util.List;

public interface DiscountService {
    DiscountResponse create(DiscountRequest request);
    DiscountResponse update(Long id, DiscountRequest request);
    void delete(Long id);
    DiscountResponse getById(Long id);
    DiscountResponse getByCode(String code);
    List<DiscountResponse> getAll();
    void updateStatuses();
}