package com.hieunguyen.controller;

import com.hieunguyen.dto.request.OrderItemRequest;
import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.entity.OrderItem;
import com.hieunguyen.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderItem>>> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemService.getAllOrderItems();
        return ResponseEntity.ok(ApiResponse.success(orderItems));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderItem>> getOrderItemById(@PathVariable Long id) {
        OrderItem orderItem = orderItemService.getOrderItemById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));
        return ResponseEntity.ok(ApiResponse.success(orderItem));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderItem>> createOrderItem(@Valid @RequestBody OrderItemRequest request) {
        OrderItem newOrderItem = orderItemService.createOrderItem(request);
        return ResponseEntity.ok(ApiResponse.success(newOrderItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderItem>> updateOrderItem(@PathVariable Long id,
                                                                  @Valid @RequestBody OrderItemRequest request) {
        OrderItem updatedOrderItem = orderItemService.updateOrderItem(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedOrderItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.ok(ApiResponse.success("OrderItem deleted successfully"));
    }
}
