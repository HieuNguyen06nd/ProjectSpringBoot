package com.hieunguyen.shopwq.service;

import com.hieunguyen.shopwq.config.JwtProvider;
import com.hieunguyen.shopwq.model.User;
import com.hieunguyen.shopwq.repository.UserRepository;
import com.hieunguyen.shopwq.service.imp.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email =  jwtProvider.getEmailFromJwtToken(jwt);
        User user = findUserByEmail(email);
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        User user = userRepository.findByEmail(email);

        if (user == null){
            throw new Exception("User not found!");
        }
        return user;
    }
}
