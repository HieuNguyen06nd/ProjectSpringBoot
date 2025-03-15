package com.hieunguyen.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    private String fileName; // Tên file lưu trữ

    private String fileType; // IMAGE hoặc VIDEO

    private String fileUrl; // Đường dẫn đến file sau khi upload
}

