package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.ReservationRequest;
import com.hieunguyen.entity.Reservation;
import com.hieunguyen.entity.Tables;
import com.hieunguyen.repository.ReservationRepository;
import com.hieunguyen.repository.TableRepository;
import com.hieunguyen.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;

    private static final int OPEN_HOUR = 10;
    private static final int CLOSE_HOUR = 22;

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(ReservationRequest request) {
        LocalDateTime now = LocalDateTime.now();

        // Kiểm tra thời gian đặt bàn có hợp lệ không
        if (request.getReservationTime().isBefore(now)) {
            throw new IllegalArgumentException("Reservation time must be in the future.");
        }

        // Kiểm tra giờ mở cửa
        int hour = request.getReservationTime().getHour();
        if (hour < OPEN_HOUR || hour >= CLOSE_HOUR) {
            throw new IllegalArgumentException("Reservations can only be made between 10:00 and 22:00.");
        }

        // Kiểm tra bàn có tồn tại không
        Tables table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new IllegalArgumentException("Table not found."));

        // Kiểm tra xung đột đặt bàn
        LocalDateTime startTime = request.getReservationTime();
        LocalDateTime endTime = startTime.plusHours(2); // Giả định mỗi lượt đặt bàn là 2 tiếng

        boolean isConflict = reservationRepository.existsByTableAndReservationTimeBetween(table, startTime, endTime);
        if (isConflict) {
            throw new IllegalArgumentException("Table is already reserved during this time.");
        }

        // Tạo mới đặt bàn
        Reservation reservation = Reservation.builder()
                .customerName(request.getCustomerName())
                .phone(request.getPhone())
                .reservationTime(request.getReservationTime())
                .table(table)
                .build();

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservation(Long id, ReservationRequest request) {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        Tables table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));

        existing.setCustomerName(request.getCustomerName());
        existing.setPhone(request.getPhone());
        existing.setReservationTime(request.getReservationTime());
        existing.setTable(table);

        return reservationRepository.save(existing);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
