package com.hieunguyen.lakeSide.service.auth;

import com.hieunguyen.lakeSide.dto.request.SignupRequest;
import com.hieunguyen.lakeSide.dto.UserDto;

public interface IAuthService {

    UserDto createUser(SignupRequest signupRequest);
}
