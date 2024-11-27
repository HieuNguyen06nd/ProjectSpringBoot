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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String token;
	
	@Column(name = "token_type", length = 50)
	String tokenType;
	
	@Column(name = "expiration_date")
	LocalDate expirationDate;

	boolean revoked;
	
	boolean expired;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	UserEntity userEntity;
}
