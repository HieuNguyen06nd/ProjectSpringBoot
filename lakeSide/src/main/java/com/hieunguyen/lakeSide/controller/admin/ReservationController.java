package com.hieunguyen.lakeSide.controller.admin;

import com.hieunguyen.lakeSide.service.admin.reservation.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class ReservationController {

    private final IReservationService reservationService;

    @GetMapping("/reservation/{numberPage}")
    public ResponseEntity<?> getAllReservation(@PathVariable int numberPage){
        try {
            return ResponseEntity.ok(reservationService.getAllReservation(numberPage));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/reservation/{id}/{status}")
    public ResponseEntity<?> updateReservationStatus(@PathVariable Long id, @PathVariable String status){
        boolean success = reservationService.updateReservationStatus(id,status);

        if (success){
            return ResponseEntity.ok("Update successfully");
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}
