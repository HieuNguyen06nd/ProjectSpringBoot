package com.hieunguyen.service.impl;

import org.springframework.stereotype.Service;

import com.hieunguyen.dto.UserDTO;
import com.hieunguyen.exception.DataNotFoundException;
import com.hieunguyen.model.UserEntity;

@Service
public interface IUserService {

	UserEntity createUser(UserDTO userDTO) throws Exception;

	String login(String phoneNumber, String password) throws Exception;
}
