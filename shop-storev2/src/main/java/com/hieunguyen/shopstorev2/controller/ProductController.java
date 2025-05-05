package com.hieunguyen.shopstorev2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hieunguyen.shopstorev2.dto.request.ProductRequest;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.ProductResponse;
import com.hieunguyen.shopstorev2.service.FileStorageService;
import com.hieunguyen.shopstorev2.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> createProduct(
            @RequestPart("data") String json,
            @RequestPart("images") List<MultipartFile> files
    ) {
        try {
            ProductRequest request = objectMapper.readValue(json, ProductRequest.class);
            Set<String> urls = files.stream()
                    .map(file -> fileStorageService.saveFile(file, "products"))
                    .collect(java.util.stream.Collectors.toSet());
            request.setImages(urls);
            return ApiResponse.success(productService.create(request));
        } catch (Exception e) {
            return ApiResponse.error(400, "Lỗi tạo sản phẩm: " + e.getMessage());
        }
    }

    @GetMapping("/my")
    public ApiResponse<List<ProductResponse>> getMyShopProducts() {
        return ApiResponse.success(productService.getMyShopProducts());
    }

    @PutMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Long productId, @RequestBody @Valid ProductRequest request) {
        return ApiResponse.success(productService.updateProduct(productId, request));
    }

    @PatchMapping("/items/{itemId}/stock")
    public ApiResponse<String> updateItemStock(@PathVariable Long itemId, @RequestParam Integer stock) {
        productService.updateStock(itemId, stock);
        return ApiResponse.success("Cập nhật số lượng thành công");
    }
}