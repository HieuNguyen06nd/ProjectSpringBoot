package com.hieunguyen.shopstorev2.repository;

import com.hieunguyen.shopstorev2.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}