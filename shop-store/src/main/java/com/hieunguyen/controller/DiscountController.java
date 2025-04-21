package com.hieunguyen.controller;

import com.hieunguyen.dto.request.DiscountRequest;
import com.hieunguyen.dto.response.DiscountResponse;
import com.hieunguyen.service.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DiscountResponse createDiscount(@Valid @RequestBody DiscountRequest request) {
        return discountService.createDiscount(request);
    }

    @PutMapping("/{id}")
    public DiscountResponse updateDiscount(@PathVariable Long id,
                                           @Valid @RequestBody DiscountRequest request) {
        return discountService.updateDiscount(id, request);
    }

    @GetMapping
    public List<DiscountResponse> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping("/{id}")
    public DiscountResponse getDiscountById(@PathVariable Long id) {
        return discountService.getDiscountById(id);
    }

    @PatchMapping("/{id}/toggle")
    public void toggleDiscountStatus(@PathVariable Long id) {
        discountService.toggleDiscountStatus(id);
    }

    @GetMapping("/product/{productId}")
    public List<DiscountResponse> getProductDiscounts(@PathVariable Long productId) {
        return discountService.getActiveProductDiscounts(productId);
    }

    @GetMapping("/order")
    public List<DiscountResponse> getOrderDiscounts() {
        return discountService.getActiveOrderDiscounts();
    }
}