package com.hieunguyen.controller;

import com.hieunguyen.dto.request.ReservationRequest;
import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.entity.Reservation;
import com.hieunguyen.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Reservation>>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(ApiResponse.success(reservations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Reservation>> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        return ResponseEntity.ok(ApiResponse.success(reservation));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Reservation>> createReservation(@Valid @RequestBody ReservationRequest request) {
        Reservation newReservation = reservationService.createReservation(request);
        return ResponseEntity.ok(ApiResponse.success(newReservation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Reservation>> updateReservation(@PathVariable Long id,
                                                                      @Valid @RequestBody ReservationRequest request) {
        Reservation updatedReservation = reservationService.updateReservation(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedReservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok(ApiResponse.success("Reservation deleted successfully"));
    }
}
