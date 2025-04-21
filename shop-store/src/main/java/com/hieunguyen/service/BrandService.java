package com.hieunguyen.service;

import com.hieunguyen.model.Brand;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BrandService {
    Brand createBrand(Brand brand);
    Brand updateBrand(Long id, Brand brand);
    void deleteBrand(Long id);
    Brand getBrandById(Long id);
    List<Brand> getAllBrands();
    void deleteBrandLogo(Long id) throws IOException;
    String uploadBrandImage(MultipartFile file) throws IOException;
}
