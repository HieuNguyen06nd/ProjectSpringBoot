package com.hieunguyen.lakeSide.repository;

import com.hieunguyen.lakeSide.enums.UserRole;
import com.hieunguyen.lakeSide.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFistByEmail(String email);

    Optional<User> findByUserRole(UserRole userRole);
}
