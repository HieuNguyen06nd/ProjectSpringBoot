package com.hieunguyen.lakeSide.service;

import com.hieunguyen.lakeSide.dto.SignupRequest;
import com.hieunguyen.lakeSide.dto.UserDto;
import com.hieunguyen.lakeSide.enums.UserRole;
import com.hieunguyen.lakeSide.model.User;
import com.hieunguyen.lakeSide.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @PostConstruct
    public void createAdminAccount(){
        Optional<User> adminAccount = userRepository.findByUserRole(UserRole.ADMIN);

        if (adminAccount.isEmpty()){
            User user = new User();

            user.setEmail("admin@test.com");
            user.setName("Admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setUserRole(UserRole.ADMIN);

            userRepository.save(user);
            System.out.println("Admin account create successfully");
        }else {
            System.out.println("Admin account already exist");
        }
    }

    public UserDto createUser(SignupRequest signupRequest){
        if (userRepository.findFistByEmail(signupRequest.getEmail()).isPresent()){
            throw new EntityExistsException("User already present with email: "+ signupRequest.getEmail());
        }else {
            User user = new User();

            user.setName(signupRequest.getName());
            user.setUserRole(UserRole.CUSTOMER);
            user.setEmail(signupRequest.getEmail());
            user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));

            User createUser = userRepository.save(user);
            return createUser.getUserDto() ;
        }
    }

}
