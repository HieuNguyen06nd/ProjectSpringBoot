package com.hieunguyen.service;

import com.hieunguyen.dto.request.ProductRequest;
import com.hieunguyen.dto.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Set;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse updateProduct(Long id, ProductRequest productRequest);

    ProductResponse getProductById(Long id);

    List<ProductResponse> getAllProducts();

    void deleteProduct(Long id);

    String uploadProductImage(MultipartFile file) throws Exception;

    List<ProductResponse> getTopSellingProducts(int limit);


}
