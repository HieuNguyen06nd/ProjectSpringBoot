package com.hieunguyen.shopstorev2.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "colors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable=false)
    private String name;

    private String hexColor;
}
