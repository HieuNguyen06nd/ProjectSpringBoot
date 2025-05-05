package com.hieunguyen.controller;

import com.hieunguyen.config.CustomUserDetails;
import com.hieunguyen.dto.request.OrderRequest;
import com.hieunguyen.dto.response.OrderResponse;
import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Tạo đơn hàng mới cho user đã đăng nhập
     * userId, customerName, customerPhone, customerAddress sẽ được lấy từ CustomUserDetails và AddressService
     */
    @PostMapping
    public ResponseData<OrderResponse> createOrder(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody OrderRequest orderRequest
    ) {
        OrderResponse response = orderService.createOrder(currentUser.getId(), orderRequest);
        return new ResponseData<>(200, "Tạo Order thành công", response);
    }

    /**
     * Lấy chi tiết đơn hàng, chỉ cho user có ROLE_ADMIN hoặc chủ đơn
     */
    @GetMapping("/{id}")
    public ResponseData<OrderResponse> getOrderById(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id
    ) {
        OrderResponse response = orderService.getOrderById(currentUser.getId(), id);
        return new ResponseData<>(200, "Chi tiết Order", response);
    }

    /**
     * Lấy tất cả đơn hàng (ADMIN) hoặc chỉ đơn của chính user
     */
    @GetMapping
    public ResponseData<List<OrderResponse>> getAllOrders(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        List<OrderResponse> responses = orderService.getAllOrders(currentUser.getId());
        return new ResponseData<>(200, "Danh sách Order", responses);
    }

    /**
     * Cập nhật đơn hàng (chỉ ADMIN hoặc chủ đơn)
     */
    @PutMapping("/{id}")
    public ResponseData<OrderResponse> updateOrder(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id,
            @Valid @RequestBody OrderRequest orderRequest
    ) {
        OrderResponse response = orderService.updateOrder(currentUser.getId(), id, orderRequest);
        return new ResponseData<>(200, "Order cập nhật thành công", response);
    }

    /**
     * Xóa đơn hàng (chỉ ADMIN hoặc chủ đơn)
     */
    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteOrder(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id
    ) {
        orderService.deleteOrder(currentUser.getId(), id);
        return new ResponseData<>(200, "Xóa Order thành công", null);
    }
}
