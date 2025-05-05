package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.User;
import com.hieunguyen.shopstorev2.utils.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailOrPhone(String email, String phone);

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderIdAndAuthProvider(String providerId, AuthProvider authProvider);


}
