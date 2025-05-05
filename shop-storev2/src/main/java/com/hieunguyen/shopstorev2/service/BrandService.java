package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.request.BrandRequest;
import com.hieunguyen.shopstorev2.dto.response.BrandResponse;

import java.util.List;

public interface BrandService {
    BrandResponse createBrand(BrandRequest request);
    List<BrandResponse> getAllBrands();
    BrandResponse updateBrand(Long id, BrandRequest request);
    void deleteBrand(Long id);
}