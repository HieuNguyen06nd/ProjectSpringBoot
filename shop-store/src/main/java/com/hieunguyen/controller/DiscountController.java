package com.hieunguyen.controller;

import com.hieunguyen.dto.request.DiscountRequest;
import com.hieunguyen.dto.response.DiscountResponse;
import com.hieunguyen.dto.response.ResponseData;
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
    public ResponseData<DiscountResponse> createDiscount(@Valid @RequestBody DiscountRequest request) {
        DiscountResponse dto = discountService.createDiscount(request);
        return new ResponseData<>(200, "Mã giảm giá được tạo thành công", dto);
    }

    @PutMapping("/{id}")
    public ResponseData<DiscountResponse> updateDiscount(@PathVariable Long id,
                                                         @Valid @RequestBody DiscountRequest request) {
        DiscountResponse dto = discountService.updateDiscount(id, request);
        return new ResponseData<>(200, "Cập nhật mã giảm giá thành công", dto);
    }

    @GetMapping
    public ResponseData<List<DiscountResponse>> getAllDiscounts() {
        List<DiscountResponse> list = discountService.getAllDiscounts();
        return new ResponseData<>(200, "Danh sách mã giảm giá", list);
    }

    @GetMapping("/{id}")
    public ResponseData<DiscountResponse> getDiscountById(@PathVariable Long id) {
        DiscountResponse dto = discountService.getDiscountById(id);
        return new ResponseData<>(200, "Thông tin mã giảm giá", dto);
    }

    @PatchMapping("/{id}/toggle")
    public ResponseData<Void> toggleDiscountStatus(@PathVariable Long id) {
        discountService.toggleDiscountStatus(id);
        return new ResponseData<>(200, "Thay đổi trạng thái thành công", null);
    }

    @GetMapping("/product/{productId}")
    public ResponseData<List<DiscountResponse>> getProductDiscounts(@PathVariable Long productId) {
        List<DiscountResponse> list = discountService.getActiveProductDiscounts(productId);
        return new ResponseData<>(200, "Danh sách mã giảm giá áp dụng cho sản phẩm", list);
    }

    @GetMapping("/order")
    public ResponseData<List<DiscountResponse>> getOrderDiscounts() {
        List<DiscountResponse> list = discountService.getActiveOrderDiscounts();
        return new ResponseData<>(200, "Danh sách mã giảm giá áp dụng cho đơn hàng", list);
    }
}
