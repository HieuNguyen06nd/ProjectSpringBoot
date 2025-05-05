package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.dto.request.OrderItemRequest;
import com.hieunguyen.shopstorev2.dto.request.OrderRequest;
import com.hieunguyen.shopstorev2.dto.response.OrderResponse;
import com.hieunguyen.shopstorev2.entities.*;
import com.hieunguyen.shopstorev2.repository.*;
import com.hieunguyen.shopstorev2.service.OrderService;
import com.hieunguyen.shopstorev2.utils.ApplicableType;
import com.hieunguyen.shopstorev2.utils.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ProductItemRepository productItemRepository;
    private final UserRepository userRepository;
    private final DiscountRepository discountRepository;
    private final DiscountUsageRepository discountUsageRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User user = getCurrentUser();
        Address address = addressRepository.findById(request.getAddressId()).orElseThrow();

        Order order = Order.builder()
                .user(user)
                .address(address)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        BigDecimal total = BigDecimal.ZERO;
        Set<OrderItem> orderItems = new HashSet<>();

        for (OrderItemRequest itemRequest : request.getItems()) {
            ProductItem item = productItemRepository.findById(itemRequest.getProductItemId()).orElseThrow();
            BigDecimal unitPrice = item.getPrice();
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productItem(item)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .build();
            orderItems.add(orderItem);
        }

        // Apply discount
        if (request.getDiscountCode() != null) {
            Discount discount = discountRepository.findByCode(request.getDiscountCode())
                    .filter(d -> d.getApplicableTo() == ApplicableType.ORDER)
                    .orElseThrow(() -> new RuntimeException("Mã giảm giá không hợp lệ hoặc không áp dụng cho đơn hàng"));

            BigDecimal originalTotal = total;

            if (discount.getPercentage() != null) {
                total = total.subtract(originalTotal.multiply(discount.getPercentage()).divide(BigDecimal.valueOf(100)));
            } else if (discount.getFixedAmount() != null) {
                total = total.subtract(BigDecimal.valueOf(discount.getFixedAmount()));
            }

            DiscountUsage usage = DiscountUsage.builder()
                    .discount(discount)
                    .order(order)
                    .appliedTo(ApplicableType.ORDER)
                    .appliedAmount(originalTotal.subtract(total))
                    .usedAt(LocalDateTime.now())
                    .build();

            discountUsageRepository.save(usage);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(total);

        return map(orderRepository.save(order));
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        User user = getCurrentUser();
        return orderRepository.findByUser(user)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        return map(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    private OrderResponse map(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
}
