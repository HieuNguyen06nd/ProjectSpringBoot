package com.hieunguyen.controller;

import com.hieunguyen.dto.request.CreateProductRequest;
import com.hieunguyen.entity.Product;
import com.hieunguyen.entity.Seller;
import com.hieunguyen.exception.ProductException;
import com.hieunguyen.service.ProductService;
import com.hieunguyen.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers/product")
@RequiredArgsConstructor
public class SellerProductController {

    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Product>> getProductBySeller(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);

        List<Product> products = productService.getProductBySellerId(seller.getId());

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);

        Product product = productService.createProduct(request, seller);

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws ProductException {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) throws ProductException {

        Product updateProduct = productService.updateProduct(id, product);
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }
}
