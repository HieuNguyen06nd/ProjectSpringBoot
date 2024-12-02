package com.hieunguyen.service;

import com.hieunguyen.dto.OrderDTO;
import com.hieunguyen.exception.DataNotFoundException;
import com.hieunguyen.model.OrderEntity;
import com.hieunguyen.model.UserEntity;
import com.hieunguyen.reponse.OrderReponse;
import com.hieunguyen.repository.OrderRepository;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.service.impl.IOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService implements IOrderService {

    UserRepository userRepository;
    OrderRepository orderRepository;
    ModelMapper modelMapper;

    @Override
    public OrderEntity createOrder(OrderDTO orderDTO) throws Exception {
       UserEntity userEntity =  userRepository.findById(orderDTO.getUserId())
               .orElseThrow( ()->new DataNotFoundException("Cannot found user!" +orderDTO.getUserId()));
//       convert dto -> order
        modelMapper.typeMap(OrderDTO.class, OrderEntity.class)
                .addMappings(mapper ->mapper.skip(OrderEntity::setId));
//        cap nhat truong don hang orderDTO
        OrderEntity orderEntity = new OrderEntity();
        modelMapper.map(orderDTO,orderEntity);
        orderEntity.setUserEntity(userEntity);
        orderEntity.setOrderDate(LocalDate.now());
//        kiem tra ngay ship > now
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate().toLocalDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today !");
        }
        orderEntity.setShippingDate(Date.valueOf(shippingDate));
        orderEntity.setActive(true);

        orderRepository.save(orderEntity);
        return orderEntity;
    }

    @Override
    public OrderEntity getOrderById(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<OrderEntity> findByUserId(Long userId) {

        return orderRepository.findByUserEntityId(userId);
    }

    @Override
    public OrderEntity updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() ->new DataNotFoundException("Cannot find order id"+id));
        UserEntity userEntity = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()->new DateTimeException("Cannot find user id "+ orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, OrderEntity.class)
                .addMappings(mapper -> mapper.skip(OrderEntity::setId));
        modelMapper.map(orderDTO,orderEntity);
        orderEntity.setUserEntity(userEntity);
        return orderRepository.save(orderEntity);
    }

    @Override
    public void deleteOrder(long id) {
//xoa mem
        OrderEntity orderEntity = orderRepository.findById(id).orElse(null);
        if (orderEntity != null){
            orderEntity.setActive(false);
            orderRepository.save(orderEntity);
        }
    }
}
