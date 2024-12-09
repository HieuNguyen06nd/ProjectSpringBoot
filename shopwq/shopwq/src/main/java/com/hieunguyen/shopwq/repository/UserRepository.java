package com.hieunguyen.shopwq.repository;

import com.hieunguyen.shopwq.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     User findByEmail(String username);
}
