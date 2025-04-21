package com.hieunguyen.model;

import com.hieunguyen.utils.ProductItemStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"product", "color", "size", "material"})
public class ProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // cột product_id

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductItemStatus status = ProductItemStatus.ACTIVE;

    // ======== Tự động sinh SKU trước khi lưu =========
    @PrePersist
    public void generateSKU() {
        if (this.sku == null || this.sku.isEmpty()) {
            // Lấy ID của product, color, size
            String productCode = (product != null ? product.getId().toString() : "P");
            String colorCode = (color != null ? color.getId().toString() : "C");
            String sizeCode = (size != null ? size.getId().toString() : "S");

            // Lấy 4 ký tự cuối timestamp để tránh quá dài
            String uniquePart = String.valueOf(System.currentTimeMillis()).substring(9);

            this.sku = productCode + colorCode + sizeCode + uniquePart;
        }
    }

}
