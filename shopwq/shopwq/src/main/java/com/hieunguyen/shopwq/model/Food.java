package com.hieunguyen.shopwq.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private Long price;

    @ManyToOne
    private Category foodCategory;

    @Column(length = 1000)
    @ElementCollection
    private List<String> images;

    private boolean available;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")  // This should map to the restaurant in the Food entity
    private Restaurent restaurant;

    private boolean isVegetarian;

    private boolean isSeasonal;

    @ManyToMany
    private List<IngredientsItems> ingredientsItems = new ArrayList<>();

    private Date createdAt;
}