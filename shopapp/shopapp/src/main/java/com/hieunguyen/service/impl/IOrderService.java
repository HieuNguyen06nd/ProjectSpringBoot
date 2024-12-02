package com.hieunguyen.service.impl;

import com.hieunguyen.dto.OrderDTO;
import com.hieunguyen.exception.DataNotFoundException;
import com.hieunguyen.model.OrderEntity;
import com.hieunguyen.reponse.OrderReponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderService {

    OrderEntity createOrder(OrderDTO orderDTO) throws Exception;

    OrderEntity getOrderById(long id);

    List<OrderEntity> findByUserId(Long user_id);

    OrderEntity updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(long id);
}
