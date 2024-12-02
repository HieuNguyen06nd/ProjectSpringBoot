package com.hieunguyen.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends BaseEntity implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String fullname;
	
	@Column(name = "phone_number", length = 10)
	String phoneNumber;
	
	String address;
	
	String password;
	
	boolean active;
	
	Date dob;
	
	@JsonProperty("faceBook_account_id")
	int facebookAccountId;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	RoleEntity roleEntity;


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_"+getRoleEntity().getName()));
		return authorityList;
	}

	@Override
	public String getUsername() {
		return phoneNumber;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
