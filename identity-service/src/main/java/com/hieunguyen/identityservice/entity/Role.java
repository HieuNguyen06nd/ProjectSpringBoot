package com.hieunguyen.identityservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private String name;
    private String description;

    @ManyToMany
    Set<Permission> permissions;

}
