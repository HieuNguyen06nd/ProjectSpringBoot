package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.request.ProductRequest;
import com.hieunguyen.shopstorev2.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    List<ProductResponse> getMyShopProducts();
    ProductResponse updateProduct(Long productId, ProductRequest request);
    void updateStock(Long itemId, Integer stock);
}