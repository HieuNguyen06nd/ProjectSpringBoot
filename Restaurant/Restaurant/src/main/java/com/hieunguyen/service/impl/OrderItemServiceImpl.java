package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.OrderItemRequest;
import com.hieunguyen.entity.Menu;
import com.hieunguyen.entity.Orders;
import com.hieunguyen.entity.OrderItem;
import com.hieunguyen.repository.MenuRepository;
import com.hieunguyen.repository.OrderItemRepository;
import com.hieunguyen.repository.OrderRepository;
import com.hieunguyen.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;


    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public Optional<OrderItem> getOrderItemById(Long id) {
        return orderItemRepository.findById(id);
    }

    @Override
    @Transactional
    public OrderItem createOrderItem(OrderItemRequest request) {
        // Tìm Order theo orderId
        Orders order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + request.getOrderId()));

        // Tìm Menu theo menuId
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + request.getMenuId()));

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .menu(menu)
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();

        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public OrderItem updateOrderItem(Long id, OrderItemRequest request) {
        OrderItem existing = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + id));

        // Cập nhật Order nếu cần
        Orders order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + request.getOrderId()));
        // Cập nhật Menu nếu cần
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + request.getMenuId()));

        existing.setOrder(order);
        existing.setMenu(menu);
        existing.setQuantity(request.getQuantity());
        existing.setPrice(request.getPrice());

        return orderItemRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteOrderItem(Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new RuntimeException("OrderItem not found with id: " + id);
        }
        orderItemRepository.deleteById(id);
    }
}
