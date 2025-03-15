package com.hieunguyen.controller;

import com.hieunguyen.dto.request.OrderRequest;
import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.entity.Orders;
import com.hieunguyen.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Orders>>> getAllOrders() {
        List<Orders> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Orders>> getOrderById(@PathVariable Long id) {
        Orders order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Orders>> createOrder(@Valid @RequestBody OrderRequest request) {
        Orders newOrder = orderService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.success(newOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Orders>> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest request) {
        Orders updatedOrder = orderService.updateOrder(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order deleted successfully"));
    }
}
