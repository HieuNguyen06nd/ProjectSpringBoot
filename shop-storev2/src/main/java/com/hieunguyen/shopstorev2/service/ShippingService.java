package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.response.ShippingStatusResponse;
import com.hieunguyen.shopstorev2.entities.Order;

public interface ShippingService {
    String createOrder(Order order);
    ShippingStatusResponse trackOrder(String trackingCode);
}

