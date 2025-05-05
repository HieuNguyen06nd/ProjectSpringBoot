package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}