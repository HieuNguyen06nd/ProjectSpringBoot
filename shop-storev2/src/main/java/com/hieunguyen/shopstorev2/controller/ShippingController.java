package com.hieunguyen.shopstorev2.controller;

import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.ShippingStatusResponse;
import com.hieunguyen.shopstorev2.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping("/track")
    public ApiResponse<ShippingStatusResponse> track(@RequestParam String trackingCode) {
        ShippingStatusResponse status = shippingService.trackOrder(trackingCode);
        return ApiResponse.success(status);
    }
}
