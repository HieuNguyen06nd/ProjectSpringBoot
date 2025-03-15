package com.hieunguyen.service;

import com.hieunguyen.entity.OrderItem;
import com.hieunguyen.dto.request.OrderItemRequest;
import java.util.List;
import java.util.Optional;

public interface OrderItemService {
    List<OrderItem> getAllOrderItems();
    Optional<OrderItem> getOrderItemById(Long id);
    OrderItem createOrderItem(OrderItemRequest request);
    OrderItem updateOrderItem(Long id, OrderItemRequest request);
    void deleteOrderItem(Long id);
}
