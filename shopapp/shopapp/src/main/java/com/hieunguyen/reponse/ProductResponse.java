package com.hieunguyen.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.hieunguyen.model.ProductEntity;
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
public class ProductResponse extends BaseReponse {

	String name;
	
	Float price;
	
	String thumbnail;
	
	String deccription;
	
	@JsonProperty("category_id")
	long categoryId;

	public static ProductResponse fromProduct(ProductEntity productEntity){
		ProductResponse productResponse =	ProductResponse.builder()
				.name(productEntity.getName())
				.price(productEntity.getPrice())
				.thumbnail(productEntity.getThumbnail())
				.deccription(productEntity.getDescription())
				.categoryId(productEntity.getCategoryEntity().getId())
				.build();
		productResponse.setCreatedAt(productEntity.getCreatedAt());
		productResponse.setUpdatedAt(productEntity.getUpdatedAt());
		return productResponse;
	}
	
}
