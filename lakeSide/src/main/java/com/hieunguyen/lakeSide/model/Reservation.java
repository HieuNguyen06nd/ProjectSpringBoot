package com.hieunguyen.lakeSide.model;

import com.hieunguyen.lakeSide.dto.ReservationDto;
import com.hieunguyen.lakeSide.dto.response.ReservationResponse;
import com.hieunguyen.lakeSide.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Long price;

    private ReservationStatus reservationStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    public ReservationDto getReservationDto(){

        ReservationDto reservationDto = new ReservationDto();

        reservationDto.setId(id);
        reservationDto.setPrice(price);
        reservationDto.setCheckInDate(checkInDate);
        reservationDto.setCheckOutDate(checkOutDate);
        reservationDto.setReservationStatus(reservationStatus);

        reservationDto.setRoomId(room.getId());
        reservationDto.setRoomName(room.getName());

        reservationDto.setUserId(user.getId());
        reservationDto.setUserName(user.getName());
        reservationDto.setRoomType(room.getType());

        return reservationDto;
    }

}
