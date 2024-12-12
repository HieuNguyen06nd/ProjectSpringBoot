package com.hieunguyen.lakeSide.dto;

import com.hieunguyen.lakeSide.enums.ReservationStatus;
import com.hieunguyen.lakeSide.model.Room;
import com.hieunguyen.lakeSide.model.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Data
public class ReservationDto {
    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Long price;

    private ReservationStatus reservationStatus;

    private Long roomId;

    private Long userId;

    private String roomName;

    private String roomType;

    private String userName;

}
