package com.hieunguyen.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.hieunguyen.dto.UserDTO;
import com.hieunguyen.exception.DataNotFoundException;
import com.hieunguyen.model.CategoryEntity;
import com.hieunguyen.model.RoleEntity;
import com.hieunguyen.model.UserEntity;
import com.hieunguyen.repository.RoleRepository;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.service.impl.IUserService;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {

	UserRepository userRepository;
	RoleRepository roleRepository;
	
	@Override
	public UserEntity createUser(UserDTO userDTO) throws DataNotFoundException {
		
		String phoneNumber = userDTO.getPhoneNumber();

		if(userRepository.existsByPhoneNumber(phoneNumber)) {
			throw new DataIntegrityViolationException("Phone number exists");
		}
		
		UserEntity userEntity = UserEntity.builder()
				.fullname(userDTO.getFullName())
				.phoneNumber(userDTO.getPhoneNumber())
				.password(userDTO.getPassword())
				.address(userDTO.getAddress())
				.dob(userDTO.getDateOfBirth())
				.facebookAccountId(userDTO.getFacebookAccountId())
				.build();

		RoleEntity roleEntity = roleRepository.findById(userDTO.getRoleId())
				.orElseThrow(()-> new DataNotFoundException("Role not found"));
		
		userEntity.setRoleEntity(roleEntity);
		
		if (userDTO.getFacebookAccountId() == 0) {
			String password = userDTO.getPassword();
//			String encodePassword = passwordEncoder.encode(password);
//			userEntity.setPassword(encodePassword);
		}
		return userRepository.save(userEntity);
	}

	@Override
	public String login(String phoneNumber, String password) {
		
		return null;
	}

}
