package com.hieunguyen.service.impl;

import com.hieunguyen.entity.*;
import com.hieunguyen.repository.AddressRepository;
import com.hieunguyen.repository.OrderItemRepository;
import com.hieunguyen.repository.OrderRepository;
import com.hieunguyen.service.OrderService;
import com.hieunguyen.utils.OrderStatus;
import com.hieunguyen.utils.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {

        if (!user.getAddress().contains(shippingAddress)){
            user.getAddress().add(shippingAddress);
        }
        Address address = addressRepository.save(shippingAddress);

        Map<Long, List<CartItem>>  itemsBySeller = cart.getCartItems().stream().collect(Collectors.
                groupingBy(item->item.getProduct().getSeller().getId()));

        Set<Order> orders = new HashSet<>();

        for (Map.Entry<Long, List<CartItem>> entry:itemsBySeller.entrySet()){
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            int totalOrderPrice = items.stream().mapToInt(
                    CartItem::getSellingPrice
            ).sum();

            int totalItem = items.stream().mapToInt(
                    CartItem::getQuantity
            ).sum();

            Order createOrder = new Order();
            createOrder.setUser(user);
            createOrder.setSellerId(sellerId);
            createOrder.setTotalItem(totalItem);
            createOrder.setTotalMrpPrice(totalOrderPrice);
            createOrder.setTotalSellingPrice(totalOrderPrice);
            createOrder.setShippingAddress(address);
            createOrder.setOrderStatus(OrderStatus.PENDING);
            createOrder.getPaymentDetail().setStatus(PaymentStatus.PENDING);

            Order saveOrder = orderRepository.save(createOrder);
            orders.add(saveOrder);

            List<OrderItem> orderItems = new ArrayList<>();

            for (CartItem item: items){
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(saveOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());

                saveOrder.getOrderItems().add(orderItem);

                OrderItem saveOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(saveOrderItem);
            }
        }
        return orders;
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepository.findById(id).orElseThrow(()->new Exception(" order notb found ..."));
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellerOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);

        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = findOrderById(orderId);
        if (user.getId().equals(order.getUser().getId())){
            throw  new Exception("You do not have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);

        return orderRepository.save(order);
    }

    @Override
    public OrderItem findById(Long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(()->new Exception(" order items not exist ..."));
    }
}
