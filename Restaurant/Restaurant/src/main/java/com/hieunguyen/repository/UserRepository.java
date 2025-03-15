package com.hieunguyen.repository;

import com.hieunguyen.entity.User;
import com.hieunguyen.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    List<User> findByRole(Role role);

    Optional<User> findByEmailOrPhone(String email, String phone);
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String token);
}
