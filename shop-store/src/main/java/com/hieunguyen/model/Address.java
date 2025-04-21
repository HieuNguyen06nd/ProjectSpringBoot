package com.hieunguyen.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người sở hữu địa chỉ

    @Column(nullable = false, length = 255)
    private String fullName; // Tên người nhận

    @Column(nullable = false, length = 20)
    private String phone; // Số điện thoại

    @Column(nullable = false, length = 255)
    private String province; // Tỉnh / Thành phố

    @Column(nullable = false, length = 255)
    private String district; // Quận / Huyện

    @Column(nullable = false, length = 255)
    private String ward; // Phường / Xã

    @Column(nullable = false, length = 500)
    private String detail; // Địa chỉ cụ thể

    @Column(nullable = false)
    private boolean isDefault = false; // Đánh dấu địa chỉ mặc định
}
