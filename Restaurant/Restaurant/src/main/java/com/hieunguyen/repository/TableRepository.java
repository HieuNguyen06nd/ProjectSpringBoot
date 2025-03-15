package com.hieunguyen.repository;

import com.hieunguyen.entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Tables, Long> {
}
