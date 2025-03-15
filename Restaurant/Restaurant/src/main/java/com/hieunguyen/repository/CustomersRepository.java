package com.hieunguyen.repository;

import com.hieunguyen.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomersRepository extends JpaRepository<Customers, Long> {
}
