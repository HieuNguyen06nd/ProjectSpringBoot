package com.hieunguyen.controller;

import com.hieunguyen.config.JwtProvider;
import com.hieunguyen.dto.response.PaymentLinkResponse;
import com.hieunguyen.entity.*;
import com.hieunguyen.service.*;
import com.hieunguyen.utils.AccountStatus;
import com.hieunguyen.utils.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final JwtProvider jwtProvider;

    @PostMapping()
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(@RequestBody Address address,
                                                                  @RequestParam PaymentMethod paymentMethod,
                                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        Set<Order> orders = orderService.createOrder(user, address, cart);

        PaymentLinkResponse response = new PaymentLinkResponse();

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> userOrderHistoryHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.userOrderHistory(user.getId());

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderItem> getOrderItemById(@RequestHeader("Authorization") String jwt,
                                                      @PathVariable Long orderId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        OrderItem orderItem = orderService.findById(orderId);
        return new ResponseEntity<>(orderItem, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@RequestHeader("Authorization") String jwt,
                                             @PathVariable Long orderId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.cancelOrder(orderId, user);

        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport sellerReport = sellerReportService.getSellerReport(seller);

        sellerReport.setCanceledOrders(sellerReport.getCanceledOrders()+ 1);
        sellerReport.setTotalRefunds(sellerReport.getTotalRefunds() + order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(sellerReport);

        return ResponseEntity.ok(order);
    }


}
