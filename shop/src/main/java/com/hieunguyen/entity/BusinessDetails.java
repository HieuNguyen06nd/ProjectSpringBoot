package com.hieunguyen.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BusinessDetails {

    private String businessName;

    private String businessEmail;

    private String businessMobile;

    private String businessAddress;

    private String logo;

    private String banner;
}
