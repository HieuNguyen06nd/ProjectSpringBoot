package com.hieunguyen.entity;

import com.hieunguyen.enums.StatusUser;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Customers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private Date registrationDate;

    private Double total_spent;

    @Enumerated(EnumType.STRING)
    private StatusUser status;
}
