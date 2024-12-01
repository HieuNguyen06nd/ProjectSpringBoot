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
import java.time.LocalDate;
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
        orderEntity.setOrderDate(LocalDate.now());
//        kiem tra ngay ship > now
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate().toLocalDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today !");
        }
        orderEntity.setShippingDate(Date.valueOf(shippingDate));
        orderEntity.setActive(true);

        orderRepository.save(orderEntity);
        modelMapper.typeMap(OrderEntity.class, OrderReponse.class);
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
