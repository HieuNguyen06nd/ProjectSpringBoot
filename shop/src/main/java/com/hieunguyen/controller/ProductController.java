package com.hieunguyen.controller;

import com.hieunguyen.entity.Product;
import com.hieunguyen.exception.ProductException;
import com.hieunguyen.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws ProductException {
        Product product = productService.findProductById(id);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam(required = false) String query){
        List<Product> products = productService.searchProduct(query);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Product>> aetAllProduct(@RequestParam(required = false) String category,
                                                       @RequestParam(required = false) String brand,
                                                       @RequestParam(required = false) String colors,
                                                       @RequestParam(required = false) String size,
                                                       @RequestParam(required = false) Integer minPrice,
                                                       @RequestParam(required = false) Integer maxPrice,
                                                       @RequestParam(required = false) Integer minDiscount,
                                                       @RequestParam(required = false) String sort,
                                                       @RequestParam(required = false) String stock,
                                                       @RequestParam(defaultValue = "0") Integer pageNumber){
        return new ResponseEntity<>(productService.getAllProduct(category,brand,colors,size,minPrice
        ,maxPrice, minDiscount, sort, stock, pageNumber), HttpStatus.OK);

    }

}
