package com.hieunguyen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.model.Brand;
import com.hieunguyen.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final ObjectMapper objectMapper;

    // Tạo mới thương hiệu: FE gửi dữ liệu thông qua multipart/form-data
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<Brand> createBrand(
            @RequestPart("brand") String brandJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        // Chuyển đổi JSON string sang đối tượng Brand
        Brand brand = objectMapper.readValue(brandJson, Brand.class);

        // Nếu có file được gửi lên, xử lý upload ảnh và gán logoUrl cho brand
        if (file != null && !file.isEmpty()) {
            // Giả sử brandService.uploadBrandImage(file) sẽ xử lý upload và trả về URL ảnh
            String logoUrl = brandService.uploadBrandImage(file);
            brand.setLogoUrl(logoUrl);
        }

        Brand savedBrand = brandService.createBrand(brand);
        return new ResponseData<>(HttpStatus.OK.value(), "Brand created successfully", savedBrand);
    }

    // Cập nhật thương hiệu: FE gửi dữ liệu mới cùng file (nếu có)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<Brand> updateBrand(
            @PathVariable Long id,
            @RequestPart("brand") String brandJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "shouldDeleteOldLogo", defaultValue = "false") boolean shouldDeleteOldLogo) throws IOException {

        // Chuyển đổi JSON string sang đối tượng Brand
        Brand brand = objectMapper.readValue(brandJson, Brand.class);

        // Nếu có file mới, xử lý upload và cập nhật logoUrl
        if (file != null && !file.isEmpty()) {
            if (shouldDeleteOldLogo) {
                brandService.deleteBrandLogo(id);
            }
            String newLogoUrl = brandService.uploadBrandImage(file);
            brand.setLogoUrl(newLogoUrl);
        }

        Brand updatedBrand = brandService.updateBrand(id, brand);
        return new ResponseData<>(HttpStatus.OK.value(), "Brand updated successfully", updatedBrand);
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteBrand(
            @PathVariable Long id,
            @RequestParam(value = "deleteLogo", defaultValue = "true") boolean deleteLogo) throws IOException {

        if (deleteLogo) {
            brandService.deleteBrandLogo(id);
        }
        brandService.deleteBrand(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Brand deleted successfully", null);
    }

    @GetMapping("/{id}")
    public ResponseData<Brand> getBrandById(@PathVariable Long id) {
        Brand brand = brandService.getBrandById(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Brand retrieved successfully", brand);
    }

    @GetMapping
    public ResponseData<List<Brand>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        return new ResponseData<>(HttpStatus.OK.value(), "Brands retrieved successfully", brands);
    }
}
