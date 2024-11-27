package com.hieunguyen.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
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
public class UserLoginDTO {

	@JsonProperty("phone_number")
	@NotBlank(message = "phone is required")
	String phoneNumber;
	
	@NotBlank(message = "password is required")
	String password;
	
}
