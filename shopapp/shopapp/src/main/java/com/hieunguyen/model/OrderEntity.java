package com.hieunguyen.model;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`order`")  // Enclose table name in backticks to escape reserved keyword
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity userEntity;
    
    String fullname;
    
    String email;
    
    @Column(name = "phone_number", length = 10)
    String phoneNumber;
    
    String address;
    
    String note;

    @Column(name = "order_date")
    LocalDate OrderDate;

    String status;

    @Column(name = "total_money")
    Integer totalMoney;
    
    @Column(name = "shipping_method")
    String shippingMethod;
    
    @Column(name = "shipping_adress")
    String shippingAdress;
    
    @Column(name = "shipping_date")
    Date shippingDate;
    
    @Column(name = "tracking_number")
    String trackingNumber;
    
    @Column(name = "payment_method")
    String paymentMethod;
    
    @Column(name = "payment_status")
    String paymentStatus;
    
    Boolean active;
}
