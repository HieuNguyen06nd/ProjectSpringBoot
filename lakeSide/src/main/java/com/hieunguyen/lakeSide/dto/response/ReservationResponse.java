package com.hieunguyen.lakeSide.dto.response;

import com.hieunguyen.lakeSide.dto.ReservationDto;
import lombok.Data;

import java.util.List;

@Data
public class ReservationResponse {

    private Integer totalPages;

    private Integer pageNumber;

    private List<ReservationDto> reservationDtoList;
}
