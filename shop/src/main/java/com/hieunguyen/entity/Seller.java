package com.hieunguyen.entity;

import com.hieunguyen.utils.AccountStatus;
import com.hieunguyen.utils.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;

import java.security.SecureRandom;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Seller {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private  Long id;

    private String sellerName;

    private String mobile;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    @Embedded
    private BankDetail bankDetails= new BankDetail();

    @OneToOne(cascade = CascadeType.ALL)
    private Address pickupAddress = new Address();

    private String GSTIN;

    private USER_ROLE role;

    private boolean isEmailVerified = false;

    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;
}
