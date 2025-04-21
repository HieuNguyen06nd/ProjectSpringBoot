package com.hieunguyen.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shippings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order; // Đơn hàng liên kết

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

    @Column(nullable = false, length = 50)
    private String shippingStatus = "PENDING"; // Trạng thái vận chuyển (PENDING, SHIPPED, DELIVERED, CANCELED)

    @Column(nullable = true)
    private LocalDateTime estimatedDelivery; // Dự kiến giao hàng
}
