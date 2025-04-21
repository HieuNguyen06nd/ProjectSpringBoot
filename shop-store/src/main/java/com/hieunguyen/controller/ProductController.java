package com.hieunguyen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hieunguyen.dto.request.ProductRequest;
import com.hieunguyen.dto.response.ProductResponse;
import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    // Tạo sản phẩm mới với ảnh
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<ProductResponse> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws Exception {

        // Chuyển đổi JSON string thành đối tượng ProductRequest
        ProductRequest productRequest = objectMapper.readValue(productJson, ProductRequest.class);

        // Nếu có file ảnh, xử lý upload và gán URL ảnh vào danh sách hình ảnh
        if (files != null && !files.isEmpty()) {
            Set<String> imageUrls = new HashSet<>();
            for (MultipartFile file : files) {
                String imageUrl = productService.uploadProductImage(file);
                imageUrls.add(imageUrl);
            }
            productRequest.setImages(imageUrls); // Gán danh sách ảnh vào request
        }

        // Tạo sản phẩm mới
        ProductResponse savedProduct = productService.createProduct(productRequest);
        return new ResponseData<>(HttpStatus.OK.value(), "Sản phẩm được tạo thành công", savedProduct);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws Exception {

        ProductRequest productRequest = objectMapper.readValue(productJson, ProductRequest.class);

        // Xử lý upload ảnh mới
        Set<String> uploadedImageUrls = new HashSet<>();
        if (files != null) {
            for (MultipartFile file : files) {
                uploadedImageUrls.add(productService.uploadProductImage(file));
            }
        }

        // Merge ảnh mới vào danh sách ảnh
        if (productRequest.getImages() == null) {
            productRequest.setImages(new HashSet<>());
        }
        productRequest.getImages().addAll(uploadedImageUrls);

        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật thành công", updatedProduct);
    }


    // Lấy chi tiết sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseData<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return new ResponseData<>(200, "Chi tiết sản phẩm", response);
    }

    // Lấy danh sách tất cả sản phẩm
    @GetMapping
    public ResponseData<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> responses = productService.getAllProducts();
        return new ResponseData<>(200, "Danh sách sản phẩm", responses);
    }

    // Xóa sản phẩm theo ID
    @DeleteMapping("/{id}")
    public ResponseData<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseData<>(200, "Sản phẩm đã được xóa", "OK");
    }

    // Lấy danh sách sản phẩm bán chạy nhất (trending)
    @GetMapping("/trending")
    public ResponseData<List<ProductResponse>> getTrendingProducts(
            @RequestParam(defaultValue = "8") int limit) {

        List<ProductResponse> trendingProducts = productService.getTopSellingProducts(limit);
        return new ResponseData<>(HttpStatus.OK.value(), "Danh sách sản phẩm bán chạy", trendingProducts);
    }



}
