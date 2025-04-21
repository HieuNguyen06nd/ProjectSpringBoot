package com.hieunguyen.service;

import com.hieunguyen.dto.response.OrderResponse;
import com.hieunguyen.dto.request.OrderRequest;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrder(Long id, OrderRequest orderRequest);
    void deleteOrder(Long id);
}
