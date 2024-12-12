package com.hieunguyen.lakeSide.controller.customer;

import com.hieunguyen.lakeSide.service.customer.room.IRoomService;
import com.hieunguyen.lakeSide.service.customer.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class RomController {
    private final IRoomService roomService;

    @GetMapping("/rooms/{pageNumber}")
    private ResponseEntity<?> getAvailableRoom (@PathVariable int pageNumber){
        return ResponseEntity.ok(roomService.getAvailableRooms(pageNumber));
    }
}
