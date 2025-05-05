package com.hieunguyen.service;

import com.hieunguyen.dto.request.OrderRequest;
import com.hieunguyen.dto.response.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(Long userId, OrderRequest orderRequest);
    OrderResponse getOrderById(Long userId, Long orderId);
    List<OrderResponse> getAllOrders(Long userId);
    OrderResponse updateOrder(Long userId, Long orderId, OrderRequest orderRequest);
    void deleteOrder(Long userId, Long orderId);
}
