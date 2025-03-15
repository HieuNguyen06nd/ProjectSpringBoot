package com.hieunguyen.repository;

import com.hieunguyen.entity.Seller;
import com.hieunguyen.utils.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);
}
