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

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService implements IOrderService {

    UserRepository userRepository;
    OrderRepository orderRepository;
    ModelMapper modelMapper;

    @Override
    public OrderReponse createOrder(OrderDTO orderDTO) throws Exception {
       UserEntity userEntity =  userRepository.findById(orderDTO.getUserId())
               .orElseThrow( ()->new DataNotFoundException("Cannot found user!" +orderDTO.getUserId()));
//       convert dto -> order
        modelMapper.typeMap(OrderDTO.class, OrderEntity.class)
                .addMappings(mapper ->mapper.skip(OrderEntity::setId));
//        cap nhat truong don hang orderDTO
        OrderEntity orderEntity = new OrderEntity();
        modelMapper.map(orderDTO,orderEntity);
        orderEntity.setUserEntity(userEntity);
        orderEntity.setOrderDate((java.sql.Date) new Date());

        Date shippingDate = orderDTO.getShippingDate();

        if (shippingDate ==null || shippingDate.before(new Date())){
            throw new DataNotFoundException("Date must be at least today !");
        }
        orderEntity.setActive(true);

        orderRepository.save(orderEntity);
        return modelMapper.map(orderEntity, OrderReponse.class);
    }

    @Override
    public OrderEntity getOrderById(long id) {
        return null;
    }

    @Override
    public List<OrderEntity> getAllOrder() {
        return List.of();
    }

    @Override
    public OrderEntity updateOrder(long id, OrderDTO orderDTO) {
        return null;
    }

    @Override
    public void deleteOrder(long id) {

    }
}
