package com.hieunguyen.repository;

import com.hieunguyen.entity.Reservation;
import com.hieunguyen.entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByTableAndReservationTimeBetween(Tables table, LocalDateTime startTime, LocalDateTime endTime);
}

