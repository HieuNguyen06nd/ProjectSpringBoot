package com.hieunguyen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {

	@JsonProperty("user_id")
	@Min(value = 1, message = "user must >0")
	Long userId;
	
	@JsonProperty("full_name")
	String fullName;
	
	String email;
	
	@JsonProperty("phone_number")
	@NotBlank(message = "phone number is required")
	String phoneNumber;
	
	String address;
	
	String note;

	String status;

	@JsonProperty( "order_date")
	LocalDate OrderDate;
	
	@JsonProperty("total_money")
	@Min(value = 0, message = "total money >0")
	Float totalMoney;
	
	@JsonProperty("shipping_method")
	String shippingMethod;
	
	@JsonProperty("shipping_adress")
	String shippingAdress;

	@JsonProperty("shipping_date")
	Date shippingDate;
	
	@JsonProperty("payment_method")
	String paymentMethod;
}
