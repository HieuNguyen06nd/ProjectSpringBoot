package com.hieunguyen.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hieunguyen.model.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderReponse extends BaseReponse{
    Long id;

    @JsonProperty("user_id")
    Long userId;

    String fullname;

    String email;

    @JsonProperty("phone_number")
    String phoneNumber;

    String address;

    String note;

    @JsonProperty( "order_date")
    Date OrderDate;

    String status;

    @JsonProperty("total_money")
    Float totalMoney;

    @JsonProperty("shipping_method")
    String shippingMethod;

    @JsonProperty( "shipping_adress")
    String shippingAdress;

    @JsonProperty("shipping_date")
    Date shippingDate;

    @JsonProperty( "tracking_number")
    String trackingNumber;

    @JsonProperty("payment_method")
    String paymentMethod;

    @JsonProperty("payment_status")
    String paymentStatus;

    Boolean active;
}
