package com.hieunguyen.service;

import com.hieunguyen.dto.request.DiscountRequest;
import com.hieunguyen.dto.response.DiscountResponse;
import com.hieunguyen.model.Discount;

import java.util.List;

public interface DiscountService {
    DiscountResponse mapToResponse(Discount d);
    DiscountResponse createDiscount(DiscountRequest req);
}