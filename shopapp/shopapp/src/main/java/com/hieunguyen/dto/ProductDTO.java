package com.hieunguyen.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
public class ProductDTO {

	@NotEmpty(message = "name prodcut not empty")
	@Size(min= 3, max = 200 , message = "Title must be 2-300 characters")
	String name;
	
	@Min(value = 0, message = "price must >0")
	Float price;
	
	String thumbnail;
	
	String description;
	
	@JsonProperty("category_id")
	long categoryId;
	
	
}
