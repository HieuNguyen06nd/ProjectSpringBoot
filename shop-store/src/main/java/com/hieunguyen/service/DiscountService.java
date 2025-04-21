package com.hieunguyen.service;

import com.hieunguyen.dto.request.DiscountRequest;
import com.hieunguyen.dto.response.DiscountResponse;
import java.util.List;

public interface DiscountService {
    DiscountResponse createDiscount(DiscountRequest request);
    DiscountResponse updateDiscount(Long id, DiscountRequest request);
    List<DiscountResponse> getAllDiscounts();
    DiscountResponse getDiscountById(Long id);
    void toggleDiscountStatus(Long id);
    List<DiscountResponse> getActiveProductDiscounts(Long productId);
    List<DiscountResponse> getActiveOrderDiscounts();
}