package com.hieunguyen.lakeSide.service.admin.reservation;

import com.hieunguyen.lakeSide.dto.response.ReservationResponse;

public interface IReservationService {
    ReservationResponse getAllReservation(int pageNumber);
    boolean updateReservationStatus( Long id, String status);
}
