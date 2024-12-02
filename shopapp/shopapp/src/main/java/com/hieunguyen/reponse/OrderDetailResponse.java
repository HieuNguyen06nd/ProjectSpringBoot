package com.hieunguyen.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hieunguyen.model.OrderDetailEntity;
import com.hieunguyen.model.OrderEntity;
import com.hieunguyen.model.ProductEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    Long id;

    @JsonProperty("order_id")
    Long orderId;

    @JsonProperty("product_id")
    Long product_id;

    float price;

    int numberofproduct;

    float totalmoney;

    public static  OrderDetailResponse fromOrderDetail(OrderDetailEntity orderDetailEntity){
        return OrderDetailResponse.builder()
                .id(orderDetailEntity.getId())
                .orderId(orderDetailEntity.getOrderEntity().getId())
                .product_id(orderDetailEntity.getProductEntity().getId())
                .price(orderDetailEntity.getPrice())
                .totalmoney(orderDetailEntity.getTotalmoney())
                .numberofproduct(orderDetailEntity.getNumberofproduct())
                .build();
    }

}
