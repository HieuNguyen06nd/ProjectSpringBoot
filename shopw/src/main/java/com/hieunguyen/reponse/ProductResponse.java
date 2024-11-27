package com.hieunguyen.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	
}
