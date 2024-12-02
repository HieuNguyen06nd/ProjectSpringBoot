package com.hieunguyen.service;

import com.hieunguyen.dto.CategoryDTO;
import com.hieunguyen.dto.OrderDetailDTO;
import com.hieunguyen.exception.DataNotFoundException;
import com.hieunguyen.model.CategoryEntity;
import com.hieunguyen.model.OrderDetailEntity;
import com.hieunguyen.model.OrderEntity;
import com.hieunguyen.model.ProductEntity;
import com.hieunguyen.repository.OrderDetailRepository;
import com.hieunguyen.repository.OrderRepository;
import com.hieunguyen.repository.ProductRepository;
import com.hieunguyen.service.impl.ICategoryService;
import com.hieunguyen.service.impl.IOrderServiceDetail;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailService implements IOrderServiceDetail {

    OrderDetailRepository orderDetailRepository;
    OrderRepository orderRepository;
    ProductRepository productRepository;

    @Override
    public OrderDetailEntity createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        OrderEntity orderEntity = orderRepository.findById(orderDetailDTO.getOrderID())
                .orElseThrow(()-> new Exception());
        ProductEntity productEntity = productRepository.findById(orderDetailDTO.getProductID())
                .orElseThrow(()-> new Exception());
        OrderDetailEntity orderDetailEntity = OrderDetailEntity.builder()
                .orderEntity(orderEntity)
                .productEntity(productEntity)
                .numberofproduct(orderDetailDTO.getNumberOfProduct())
                .totalmoney(orderDetailDTO.getTotalMoney())
                .price(orderDetailDTO.getPrice())
                .build();
        return orderDetailRepository.save(orderDetailEntity);
    }

    @Override
    public OrderDetailEntity getOrderDetailById(long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(()->new  DataNotFoundException("Cannot find order detail id + "+ id));
    }

    @Override
    public List<OrderDetailEntity> getOrderfindId(Long order_id) {
        return orderDetailRepository.findByOrderEntityId(order_id);
    }

    @Override
    public OrderDetailEntity updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetailEntity orderDetailEntity = orderDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find order detail " + id));

        OrderEntity orderEntity = orderRepository.findById(orderDetailDTO.getOrderID())
                .orElseThrow(()-> new DataNotFoundException("Cannot find order id: " + orderDetailDTO.getOrderID()));
        ProductEntity productEntity = productRepository.findById(orderDetailDTO.getProductID())
                .orElseThrow(()-> new DataNotFoundException("cannot find product id: " + orderDetailDTO.getProductID()));

        orderDetailEntity.setPrice(orderDetailDTO.getPrice());
        orderDetailEntity.setNumberofproduct(orderDetailDTO.getNumberOfProduct());
        orderDetailEntity.setProductEntity(productEntity);
        orderDetailEntity.setOrderEntity(orderEntity);
        orderDetailEntity.setTotalmoney(orderDetailDTO.getTotalMoney());
        return orderDetailRepository.save(orderDetailEntity);
    }

    @Override
    public void deleteOrderDetail(long id) {
        orderDetailRepository.deleteById(id);
    }
}
