package com.hieunguyen.service;

import com.hieunguyen.entity.*;
import com.hieunguyen.utils.OrderStatus;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    Order findOrderById (Long id) throws Exception;
    List<Order> userOrderHistory( Long userId);
    List<Order> sellerOrder(Long sellerId);
    Order updateOrderStatus (Long orderId, OrderStatus orderStatus) throws Exception;
    Order cancelOrder (Long orderId, User user) throws Exception;
    OrderItem findById(Long id) throws Exception;
}
