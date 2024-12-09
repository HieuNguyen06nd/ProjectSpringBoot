package com.hieunguyen.shopwq.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")  // This should map to the restaurant in the Food entity
    private Restaurent restaurant;

    private Long totalAmount;

    private String orderStatus;

    private Date createdAt;

    @ManyToOne
    private  Address deliveryAddress;

    @OneToMany
    private List<OrderItem> items;

//    private Payment payment;

    private int totalItem;

    private int totalPrice;


}
