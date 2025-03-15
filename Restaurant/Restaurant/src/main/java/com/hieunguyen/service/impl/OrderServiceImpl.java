package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.OrderRequest;
import com.hieunguyen.entity.Orders;
import com.hieunguyen.entity.Tables;
import com.hieunguyen.repository.OrderRepository;
import com.hieunguyen.repository.TableRepository;
import com.hieunguyen.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Orders> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Orders createOrder(OrderRequest request) {
        // Tìm Table dựa trên tableId từ request
        Tables table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));
        // Nếu orderTime không được truyền, đặt mặc định là thời gian hiện tại
        LocalDateTime orderTime = request.getOrderTime() != null ? request.getOrderTime() : LocalDateTime.now();
        Orders order = Orders.builder()
                .orderTime(orderTime)
                .table(table)
                .build();
        return orderRepository.save(order);
    }

    @Override
    public Orders updateOrder(Long id, OrderRequest request) {
        Orders existing = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        // Tìm Table theo tableId từ request
        Tables table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));
        existing.setOrderTime(request.getOrderTime() != null ? request.getOrderTime() : existing.getOrderTime());
        existing.setTable(table);
        return orderRepository.save(existing);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
