package com.hieunguyen.shopwq.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Embeddable
public class RestaurantDto {
    private String title;

    @Column(length = 1000)  // Cột images sẽ lưu trữ danh sách ảnh
    private List<String> images;

    private String description;

    @Column(name = "restaurant_id")
    private Long id;

}
