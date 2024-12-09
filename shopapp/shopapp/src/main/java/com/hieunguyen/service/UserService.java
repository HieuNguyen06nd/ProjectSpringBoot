package com.hieunguyen.service;

import com.hieunguyen.component.JwtTokenUtil;
import com.hieunguyen.exception.PermissionDenyException;
import org.apache.catalina.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import java.util.Optional;

@Builder
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {

	UserRepository userRepository;
	RoleRepository roleRepository;
	PasswordEncoder passwordEncoder;
	JwtTokenUtil jwtTokenUtil;
	AuthenticationManager authenticationManager;
	
	@Override
	public UserEntity createUser(UserDTO userDTO) throws Exception {
		
		String phoneNumber = userDTO.getPhoneNumber();

		if(userRepository.existsByPhoneNumber(phoneNumber)) {
			throw new DataIntegrityViolationException("Phone number exists");
		}
		RoleEntity roleEntity = roleRepository.findById(userDTO.getRoleId())
				.orElseThrow(()-> new DataNotFoundException("Role not found"));

		if (roleEntity.getName().toUpperCase().equals(RoleEntity.ADMIN)){
			throw new PermissionDenyException("You cannot register ADMIN account");
		}
		UserEntity userEntity = UserEntity.builder()
				.fullname(userDTO.getFullName())
				.phoneNumber(userDTO.getPhoneNumber())
				.password(userDTO.getPassword())
				.address(userDTO.getAddress())
				.dob(userDTO.getDateOfBirth())
				.facebookAccountId(userDTO.getFacebookAccountId())
				.build();

		
		userEntity.setRoleEntity(roleEntity);
		
		if (userDTO.getFacebookAccountId() == 0) {
			String password = userDTO.getPassword();
			String encodePassword = passwordEncoder.encode(password);
			userEntity.setPassword(encodePassword);
		}
		return userRepository.save(userEntity);
	}

	@Override
	public String login(String phoneNumber, String password) throws Exception {
		Optional<UserEntity> optionalUserEntity =  userRepository.findByPhoneNumber(phoneNumber);
		if (optionalUserEntity.isEmpty()){
			throw  new DataNotFoundException("Phone Number or password invalid");
		}
		UserEntity userEntity = optionalUserEntity.get();

//		check password
		if (userEntity.getFacebookAccountId() == 0) {
			if (!passwordEncoder.matches(password, userEntity.getPassword())){
				throw new DataNotFoundException("Phone Number or Password is wrong");
			}
		}
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				phoneNumber, password, userEntity.getAuthorities()
		);
//		? autheticate java spring
		authenticationManager.authenticate(authenticationToken);
		return jwtTokenUtil.generateToken(userEntity);
	}

}
