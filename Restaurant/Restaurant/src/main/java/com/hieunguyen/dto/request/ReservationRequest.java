package com.hieunguyen.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReservationRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotNull(message = "Reservation time is required")
    private LocalDateTime reservationTime;

    @NotNull(message = "Table ID is required")
    private Long tableId;
}
