package com.hieunguyen.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderTime;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private Tables table;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

}
