package com.hieunguyen.shopwq.service.imp;

import com.hieunguyen.shopwq.model.User;

public interface IUserService {
    User findUserByJwtToken(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;
}
