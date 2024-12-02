package com.hieunguyen.service.impl;

import com.hieunguyen.dto.OrderDTO;
import com.hieunguyen.dto.OrderDetailDTO;
import com.hieunguyen.exception.DataNotFoundException;
import com.hieunguyen.model.OrderDetailEntity;
import com.hieunguyen.model.OrderEntity;

import java.util.List;

public interface IOrderServiceDetail {
    OrderDetailEntity createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;

    OrderDetailEntity getOrderDetailById(long id) throws DataNotFoundException;

    List<OrderDetailEntity> getOrderfindId(Long order_id);

    OrderDetailEntity updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    void deleteOrderDetail(long id);
}
