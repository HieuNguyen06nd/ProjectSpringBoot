package com.hieunguyen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailDTO {
	
	@JsonProperty("order_id")
	@Min(value = 1, message = "user must >0")
	Long orderID;
	
	@JsonProperty("product_id")
	Long productID;
	
	@Min(value = 0, message = "total money >0")
	Float price;
	
	@JsonProperty("number_of_product")
	int numberOfProduct;
	
	@JsonProperty("total_money")
	@Min(value = 0, message = "total money >0")
	float totalMoney;
	
	String color;
	
}
