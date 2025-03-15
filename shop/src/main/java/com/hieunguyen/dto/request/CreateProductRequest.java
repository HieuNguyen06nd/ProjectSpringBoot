package com.hieunguyen.dto.request;

import com.hieunguyen.entity.Category;
import com.hieunguyen.entity.Review;
import com.hieunguyen.entity.Seller;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateProductRequest {

    private  String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private String color;
    private List<String> images = new ArrayList<>();
    private String category;
    private String category2;
    private String category3;

    private String size;
}
