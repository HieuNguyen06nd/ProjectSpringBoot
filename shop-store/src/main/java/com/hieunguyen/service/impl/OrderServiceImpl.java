package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.OrderItemRequest;
import com.hieunguyen.dto.request.OrderRequest;
import com.hieunguyen.dto.response.OrderResponse;
import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.*;
import com.hieunguyen.repository.*;
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
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Lấy User và Address từ DB
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Address address = addressRepository.findById(orderRequest.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Tạo đơn hàng mới
        Orders order = Orders.builder()
                .user(user)
                .address(address)
                .status(OrderStatus.PENDING)
                .totalPrice(0) // Tính sau
                .build();

        // Xử lý danh sách OrderItem và tính tổng tiền
        double totalPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemReq : orderRequest.getOrderItems()) {
            ProductItem productItem = productItemRepository.findById(itemReq.getProductItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductItem not found"));

            double itemPrice = productItem.getPrice();

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .productItem(productItem)
                    .quantity(itemReq.getQuantity())
                    .price(itemPrice)
                    .build();

            orderItems.add(item);
            totalPrice += itemPrice * itemReq.getQuantity();
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);


        // Nếu có discount, tạo OrderDiscount và thêm vào đơn hàng
        if (orderRequest.getDiscountId() != null && orderRequest.getDiscountAmount() != null) {
            Discount discount = discountRepository.findById(orderRequest.getDiscountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));
            OrderDiscount orderDiscount = OrderDiscount.builder()
                    .order(order)
                    .discount(discount)
                    .discountAmount(orderRequest.getDiscountAmount())
                    .build();
            // Giả sử Orders có trường orderDiscounts kiểu Set<OrderDiscount>
            order.setOrderDiscounts(Set.of(orderDiscount));
            // Giảm tổng tiền theo discount
            order.setTotalPrice(totalPrice - orderRequest.getDiscountAmount());
        }

        Orders savedOrder = orderRepository.save(order);
        return convertToOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return convertToOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        Orders existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Cập nhật các thông tin cơ bản (nếu cần)
        if (orderRequest.getAddressId() != null) {
            Address address = addressRepository.findById(orderRequest.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
            existingOrder.setAddress(address);
        }
        // Cập nhật discount nếu có
        if (orderRequest.getDiscountId() != null && orderRequest.getDiscountAmount() != null) {
            Discount discount = discountRepository.findById(orderRequest.getDiscountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));
            OrderDiscount orderDiscount = OrderDiscount.builder()
                    .order(existingOrder)
                    .discount(discount)
                    .discountAmount(orderRequest.getDiscountAmount())
                    .build();
            existingOrder.setOrderDiscounts(Set.of(orderDiscount));
            // Giảm tổng tiền theo discount
            existingOrder.setTotalPrice(existingOrder.getTotalPrice() - orderRequest.getDiscountAmount());
        }
        existingOrder.setUpdatedAt(LocalDateTime.now());

        Orders updatedOrder = orderRepository.save(existingOrder);
        return convertToOrderResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    private OrderResponse convertToOrderResponse(Orders order) {
        Long discountId = null;
        Double discountAmount = null;
        if (order.getOrderDiscounts() != null && !order.getOrderDiscounts().isEmpty()) {
            OrderDiscount od = order.getOrderDiscounts().iterator().next();
            discountId = od.getDiscount().getId();
            discountAmount = od.getDiscountAmount();
        }

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .addressId(order.getAddress() != null ? order.getAddress().getId() : null)
                .customerName(order.getAddress() != null ? order.getAddress().getFullName() : "")
                .customerPhone(order.getAddress() != null ? order.getAddress().getPhone() : "")
                .customerAddress(order.getAddress() != null ? order.getAddress().getDetail() : "")
                .status(order.getStatus().name())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .discountId(discountId)
                .discountAmount(discountAmount)
                .orderItems(order.getOrderItems().stream().map(item -> {
                    ProductItem productItem = item.getProductItem();
                    return OrderResponse.OrderItemResponse.builder()
                            .id(item.getId())
                            .productItemId(productItem.getId())
                            .productId(productItem.getProduct().getId())
                            .productName(productItem.getProduct().getName())
                            .colorId(productItem.getColor().getId())
                            .colorName(productItem.getColor().getName())
                            .sizeId(productItem.getSize().getId())
                            .sizeName(productItem.getSize().getName())
                            .materialId(productItem.getMaterial().getId())
                            .materialName(productItem.getMaterial().getName())
                            .sku(productItem.getSku())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build();
                }).collect(Collectors.toList()))
                .build();
    }


}
