package com.hieunguyen.identityservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    private String name;
    private String description;
}
