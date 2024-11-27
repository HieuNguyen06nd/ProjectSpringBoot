package com.hieunguyen.dto;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserDTO {
	@JsonProperty("fullname")
	String fullName;
	
	@JsonProperty("phone_number")
	@NotBlank(message = "phone is required")
	String phoneNumber;
	
	String address;
	
	@NotBlank(message = "password is required")
	String password;
	
	@JsonProperty("retype_password")
	String retypePassword;
	
	@JsonProperty("date_of_birth")
	Date dateOfBirth;
	
	@JsonProperty("faceBook_account_id")
	int facebookAccountId;
	
	@NotNull(message = "role is required")
	@JsonProperty("role_id")
	Long roleId;
}
