package com.hieunguyen.controller;

import com.hieunguyen.entity.Order;
import com.hieunguyen.entity.Seller;
import com.hieunguyen.service.OrderService;
import com.hieunguyen.service.SellerService;
import com.hieunguyen.utils.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api//seller/orders")
public class SellerOrderController {
    private final OrderService orderService;
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrderHandler(@RequestHeader("Authorization") String jwt)
            throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);

        List<Order> orders = orderService.sellerOrder(seller.getId());

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(@RequestHeader("Authorization") String jwt,
                                                    @PathVariable Long orderId,
                                                    @PathVariable OrderStatus status) throws Exception {
        Order order = orderService.updateOrderStatus(orderId, status);

        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

}
