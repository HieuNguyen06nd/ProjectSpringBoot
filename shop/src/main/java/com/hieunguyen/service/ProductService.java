package com.hieunguyen.service;

import com.hieunguyen.dto.request.CreateProductRequest;
import com.hieunguyen.entity.Product;
import com.hieunguyen.entity.Seller;
import com.hieunguyen.exception.ProductException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Product createProduct (CreateProductRequest request , Seller seller) throws IllegalAccessException;
    void deleteProduct(Long id) throws ProductException;
    Product updateProduct(Long id, Product product) throws ProductException;
    Product findProductById( Long id) throws ProductException;
    List<Product> searchProduct(String query);
    Page<Product> getAllProduct(
            String category,
            String brand,
            String colors,
            String size,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );

    List<Product> getProductBySellerId(Long sellerId);
}
