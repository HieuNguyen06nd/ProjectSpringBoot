package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.Address;
import com.hieunguyen.shopstorev2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
