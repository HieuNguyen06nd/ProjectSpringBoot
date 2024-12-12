package com.hieunguyen.lakeSide.service.customer.booking;

import com.hieunguyen.lakeSide.dto.ReservationDto;
import com.hieunguyen.lakeSide.dto.response.ReservationResponse;

public interface IBookingService {
    boolean postReservation(ReservationDto reservationDto);
    ReservationResponse getAllReservationByUserId(Long userId, int numberPage);
}
