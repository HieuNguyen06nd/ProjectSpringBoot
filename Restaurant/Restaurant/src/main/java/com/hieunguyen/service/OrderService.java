package com.hieunguyen.service;

import com.hieunguyen.entity.Orders;
import com.hieunguyen.dto.request.OrderRequest;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Orders> getAllOrders();
    Optional<Orders> getOrderById(Long id);
    Orders createOrder(OrderRequest request);
    Orders updateOrder(Long id, OrderRequest request);
    void deleteOrder(Long id);
}
