package com.hieunguyen.lakeSide.service.iml;

import com.hieunguyen.lakeSide.dto.request.SignupRequest;
import com.hieunguyen.lakeSide.dto.UserDto;

public interface IAuthService {

    UserDto createUser(SignupRequest signupRequest);
}
