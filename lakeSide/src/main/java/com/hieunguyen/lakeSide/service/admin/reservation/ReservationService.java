package com.hieunguyen.lakeSide.service.admin.reservation;

import com.hieunguyen.lakeSide.dto.response.ReservationResponse;
import com.hieunguyen.lakeSide.enums.ReservationStatus;
import com.hieunguyen.lakeSide.model.Reservation;
import com.hieunguyen.lakeSide.model.Room;
import com.hieunguyen.lakeSide.repository.ReservationRepository;
import com.hieunguyen.lakeSide.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService{

    private  final ReservationRepository reservationRepository;

    private final RoomRepository roomRepository;

    public static  final  int SEARCH_PAGE = 4;

    public ReservationResponse getAllReservation(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, SEARCH_PAGE);

        Page<Reservation> reservationPage = reservationRepository.findAll(pageable);

        ReservationResponse reservationResponse = new ReservationResponse();

        reservationResponse.setReservationDtoList(reservationPage.stream()
                .map(Reservation::getReservationDto).collect(Collectors.toList()));

        reservationResponse.setPageNumber(reservationPage.getPageable().getPageNumber());
        reservationResponse.setTotalPages(reservationResponse.getTotalPages());

        return reservationResponse;

    }

    public boolean updateReservationStatus( Long id, String status){
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (optionalReservation.isPresent()){
            Reservation existingReservation = optionalReservation.get();
            if (Objects.equals(status,"Approve")){
                existingReservation.setReservationStatus(ReservationStatus.APPROVED);
            }else {
                existingReservation.setReservationStatus(ReservationStatus.REJECTED);
            }

            reservationRepository.save(existingReservation);

            Room existingRoom = existingReservation.getRoom();
            existingRoom.setAvailable(false);

            roomRepository.save(existingRoom);

            return true;
        }

        return false;

    }
}
