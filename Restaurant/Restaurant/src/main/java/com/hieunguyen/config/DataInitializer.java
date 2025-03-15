package com.hieunguyen.config;

import com.hieunguyen.entity.User;
import com.hieunguyen.enums.Role;
import com.hieunguyen.enums.StatusUser;
import com.hieunguyen.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByRole(Role.MANAGER).isEmpty()) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@restaurant.com");
                admin.setPhone("1234567890");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.MANAGER);
                admin.setSalary(String.valueOf(10000.0));
                admin.setStatus(StatusUser.valueOf("ACTIVE"));
                userRepository.save(admin);
                System.out.println("Default MANAGER account created: admin@restaurant.com / admin123");
            }
        };
    }
}
