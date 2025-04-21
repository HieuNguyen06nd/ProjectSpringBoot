package com.hieunguyen.controller;

import com.hieunguyen.dto.request.OrderRequest;
import com.hieunguyen.dto.response.OrderResponse;
import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Tạo đơn hàng mới
    @PostMapping
    public ResponseData<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.createOrder(orderRequest);
        return new ResponseData<>(200, "Tạo Order thành công", response);
    }

    // Lấy đơn hàng theo ID
    @GetMapping("/{id}")
    public ResponseData<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return new ResponseData<>(200, "Chi tiết Order", response);
    }

    // Lấy tất cả đơn hàng
    @GetMapping
    public ResponseData<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> responses = orderService.getAllOrders();
        return new ResponseData<>(200, "Chi tiết Order", responses);
    }

    // Cập nhật đơn hàng theo ID
    @PutMapping("/{id}")
    public ResponseData<OrderResponse> updateOrder(@PathVariable Long id,
                                                     @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.updateOrder(id, orderRequest);
        if (response == null){
            return new ResponseData<>(404, "Order không tồn tại", null);
        }
        return new ResponseData<>(200, "Order cập nhật thành công", response);
    }

    // Xoá đơn hàng theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
