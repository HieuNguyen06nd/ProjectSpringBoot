package com.hieunguyen.lakeSide.controller.customer;

import com.hieunguyen.lakeSide.dto.ReservationDto;
import com.hieunguyen.lakeSide.service.customer.booking.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class BookingController {
    private final IBookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<?> postBooking(@RequestBody ReservationDto reservationDto){
        boolean success = bookingService.postReservation(reservationDto);
        if (success) {
            return ResponseEntity.ok("Booking successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/book/{userId}/{pageNumber}")
    public ResponseEntity<?> getAllReservationByUserId(@PathVariable Long userId, @PathVariable int pageNumber){
        try {
            return ResponseEntity.ok(bookingService.getAllReservationByUserId(userId,pageNumber));
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}

