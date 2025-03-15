package com.hieunguyen.service;

import com.hieunguyen.entity.Reservation;
import com.hieunguyen.dto.request.ReservationRequest;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
    List<Reservation> getAllReservations();
    Optional<Reservation> getReservationById(Long id);
    Reservation createReservation(ReservationRequest request);
    Reservation updateReservation(Long id, ReservationRequest request);
    void deleteReservation(Long id);
}
