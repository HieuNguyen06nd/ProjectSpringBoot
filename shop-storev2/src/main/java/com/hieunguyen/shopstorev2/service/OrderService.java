package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.request.OrderRequest;
import com.hieunguyen.shopstorev2.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Long id);
    void cancelOrder(Long id);
}