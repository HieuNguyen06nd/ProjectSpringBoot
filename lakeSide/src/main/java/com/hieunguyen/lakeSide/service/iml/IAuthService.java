package com.hieunguyen.lakeSide.service.iml;

import com.hieunguyen.lakeSide.dto.SignupRequest;
import com.hieunguyen.lakeSide.dto.UserDto;
import com.hieunguyen.lakeSide.model.User;

import java.util.Optional;

public interface IAuthService {

    UserDto createUser(SignupRequest signupRequest);
}
