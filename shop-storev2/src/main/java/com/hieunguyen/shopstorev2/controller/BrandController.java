// Updated BrandController.java
package com.hieunguyen.shopstorev2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hieunguyen.shopstorev2.dto.request.BrandRequest;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.BrandResponse;
import com.hieunguyen.shopstorev2.service.BrandService;
import com.hieunguyen.shopstorev2.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BrandResponse> create(@RequestPart("data") String data,
                                             @RequestPart("file") MultipartFile file) {
        try {
            BrandRequest request = objectMapper.readValue(data, BrandRequest.class);
            String path = fileStorageService.saveFile(file, "brands");
            request.setImage(path);
            return ApiResponse.success(brandService.createBrand(request));
        } catch (Exception e) {
            return ApiResponse.error(400, "Invalid request format: " + e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<BrandResponse>> getAll() {
        return ApiResponse.success(brandService.getAllBrands());
    }

    @PutMapping("/{id}")
    public ApiResponse<BrandResponse> update(@PathVariable Long id, @RequestBody BrandRequest request) {
        return ApiResponse.success(brandService.updateBrand(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ApiResponse.success("Xóa thương hiệu thành công");
    }
}
