// DiscountController.java
package com.hieunguyen.shopstorev2.controller;

import com.hieunguyen.shopstorev2.dto.request.DiscountRequest;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.DiscountResponse;
import com.hieunguyen.shopstorev2.service.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping
    public ApiResponse<DiscountResponse> create(@RequestBody @Valid DiscountRequest request) {
        return ApiResponse.success(discountService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<DiscountResponse> update(@PathVariable Long id, @RequestBody @Valid DiscountRequest request) {
        return ApiResponse.success(discountService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        discountService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<DiscountResponse> getOne(@PathVariable Long id) {
        return ApiResponse.success(discountService.getById(id));
    }

    @GetMapping("/code/{code}")
    public ApiResponse<DiscountResponse> getByCode(@PathVariable String code) {
        return ApiResponse.success(discountService.getByCode(code));
    }

    @GetMapping
    public ApiResponse<List<DiscountResponse>> getAll() {
        return ApiResponse.success(discountService.getAll());
    }

    @PostMapping("/update-statuses")
    public ApiResponse<Void> updateStatuses() {
        discountService.updateStatuses();
        return ApiResponse.success(null);
    }
}
