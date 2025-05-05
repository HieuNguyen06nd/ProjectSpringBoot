package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.OrderItemRequest;
import com.hieunguyen.dto.request.OrderRequest;
import com.hieunguyen.dto.response.OrderResponse;
import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.Address;
import com.hieunguyen.model.Discount;
import com.hieunguyen.model.DiscountProductRule;
import com.hieunguyen.model.OrderItem;
import com.hieunguyen.model.Orders;
import com.hieunguyen.model.ProductItem;
import com.hieunguyen.model.User;
import com.hieunguyen.repository.AddressRepository;
import com.hieunguyen.repository.DiscountRepository;
import com.hieunguyen.repository.OrderRepository;
import com.hieunguyen.repository.ProductItemRepository;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.service.OrderService;
import com.hieunguyen.utils.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final DiscountRepository discountRepository;
    private final ProductItemRepository productItemRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        Address address = addressRepository.findById(orderRequest.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + orderRequest.getAddressId()));

        Orders order = Orders.builder()
                .user(user)
                .address(address)
                .status(OrderStatus.PENDING)
                .totalPrice(0.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        double totalPrice = 0;
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemRequest itemReq : orderRequest.getOrderItems()) {
            ProductItem pi = productItemRepository.findById(itemReq.getProductItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductItem not found with ID: " + itemReq.getProductItemId()));
            double price = pi.getPrice();
            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .productItem(pi)
                    .quantity(itemReq.getQuantity())
                    .price(price)
                    .build();
            items.add(oi);
            totalPrice += price * itemReq.getQuantity();
        }
        order.setOrderItems(items);
        order.setTotalPrice(totalPrice);

        if (orderRequest.getDiscountId() != null) {
            Discount discount = discountRepository.findById(orderRequest.getDiscountId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Discount not found with ID: " + orderRequest.getDiscountId()));

            // Lấy cấu hình order‐type đầu tiên
            DiscountProductRule cfg = discount.getOrderDiscounts().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No order‐type discount configured for discountId=" + discount.getId()));

            // Tính số tiền giảm
            double discountAmount = cfg.calculateDiscountAmount(totalPrice);

            // Lưu vào Orders
            order.setDiscount(discount);            // requires Orders.discount field
            order.setDiscountAmount(discountAmount);// requires Orders.discountAmount field

            // Trừ vào tổng tiền
            order.setTotalPrice(totalPrice - discountAmount);
        }
        Orders saved = orderRepository.save(order);
        return convertToOrderResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long userId, Long orderId) {
        // Optionally check ownership/admin rights
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("No permission to view this order");
        }
        return convertToOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(Long userId) {
        // If admin, fetch all; else only user's orders
        boolean isAdmin = userRepository.findById(userId)
                .map(u -> u.getRole().name().equals("ADMIN"))
                .orElse(false);
        List<Orders> list = isAdmin
                ? orderRepository.findAll()
                : orderRepository.findByUserId(userId);

        return list.stream().map(this::convertToOrderResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long userId, Long orderId, OrderRequest orderRequest) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("No permission to update this order");
        }
        if (orderRequest.getAddressId() != null) {
            Address addr = addressRepository.findById(orderRequest.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + orderRequest.getAddressId()));
            order.setAddress(addr);
        }
        if (orderRequest.getDiscountId() != null && orderRequest.getDiscountAmount() != null) {
            Discount d = discountRepository.findById(orderRequest.getDiscountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Discount not found with ID: " + orderRequest.getDiscountId()));
            DiscountProductRule od = DiscountProductRule.builder()
                    .order(order)
                    .discount(d)
                    .discountAmount(orderRequest.getDiscountAmount())
                    .build();
            order.setOrderDiscounts(Set.of(od));
            order.setTotalPrice(order.getTotalPrice() - orderRequest.getDiscountAmount());
        }
        order.setUpdatedAt(LocalDateTime.now());
        Orders updated = orderRepository.save(order);
        return convertToOrderResponse(updated);
    }

    @Override
    @Transactional
    public void deleteOrder(Long userId, Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("No permission to delete this order");
        }
        orderRepository.delete(order);
    }

    private OrderResponse convertToOrderResponse(Orders order) {
        OrderResponse.OrderItemResponse itemResp;
        Double discountAmt = null;
        Long discountId = null;
        if (order.getOrderDiscounts() != null && !order.getOrderDiscounts().isEmpty()) {
            var od = order.getOrderDiscounts().iterator().next();
            discountId = od.getDiscount().getId();
            discountAmt = od.getDiscountAmount();
        }
        List<OrderResponse.OrderItemResponse> items = order.getOrderItems().stream().map(oi -> {
            ProductItem pi = oi.getProductItem();
            return OrderResponse.OrderItemResponse.builder()
                    .id(oi.getId())
                    .productItemId(pi.getId())
                    .productId(pi.getProduct().getId())
                    .productName(pi.getProduct().getName())
                    .colorId(pi.getColor().getId())
                    .colorName(pi.getColor().getName())
                    .sizeId(pi.getSize().getId())
                    .sizeName(pi.getSize().getName())
                    .sku(pi.getSku())
                    .quantity(oi.getQuantity())
                    .price(oi.getPrice())
                    .build();
        }).collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .addressId(order.getAddress().getId())
                .customerName(order.getAddress().getFullName())
                .customerPhone(order.getAddress().getPhone())
                .customerAddress(order.getAddress().getDetail())
                .status(order.getStatus().name())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .discountId(discountId)
                .discountAmount(discountAmt)
                .orderItems(items)
                .build();
    }
}