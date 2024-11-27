package com.hieunguyen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hieunguyen.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	boolean existsByPhoneNumber (String phoneNumber);
	
	Optional<UserEntity> findByPhoneNumber(String phoneNumber);
}
