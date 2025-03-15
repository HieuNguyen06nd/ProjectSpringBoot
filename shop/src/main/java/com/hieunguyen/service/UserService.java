package com.hieunguyen.service;

import com.hieunguyen.entity.User;

public interface UserService {

     User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
