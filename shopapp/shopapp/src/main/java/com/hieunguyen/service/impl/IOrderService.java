package com.hieunguyen.service.impl;

import com.hieunguyen.dto.OrderDTO;
import com.hieunguyen.model.OrderEntity;
import com.hieunguyen.reponse.OrderReponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderService {

    OrderReponse createOrder(OrderDTO orderDTO) throws Exception;

    OrderEntity getOrderById(long id);

    List<OrderEntity> getAllOrder();

    OrderEntity updateOrder(long id, OrderDTO orderDTO);

    void deleteOrder(long id);
}
