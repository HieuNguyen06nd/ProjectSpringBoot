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
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private String phone;

    private LocalDateTime reservationTime;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private Tables table;
}
