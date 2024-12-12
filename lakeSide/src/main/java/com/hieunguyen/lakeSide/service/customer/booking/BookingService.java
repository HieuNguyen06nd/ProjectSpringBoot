package com.hieunguyen.lakeSide.service.customer.booking;

import com.hieunguyen.lakeSide.dto.ReservationDto;
import com.hieunguyen.lakeSide.dto.response.ReservationResponse;
import com.hieunguyen.lakeSide.enums.ReservationStatus;
import com.hieunguyen.lakeSide.model.Reservation;
import com.hieunguyen.lakeSide.model.Room;
import com.hieunguyen.lakeSide.model.User;
import com.hieunguyen.lakeSide.repository.ReservationRepository;
import com.hieunguyen.lakeSide.repository.RoomRepository;
import com.hieunguyen.lakeSide.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService{

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;

    private final ReservationRepository reservationRepository;

    public static  final  int SEARCH_PAGE = 4;

    public boolean postReservation(ReservationDto reservationDto){
        Optional<User> optionalUser = userRepository.findById(reservationDto.getUserId());
        Optional<Room>optionalRoom =roomRepository.findById(reservationDto.getRoomId());

        if (optionalRoom.isPresent() && optionalUser.isPresent()){
            Reservation reservation = new Reservation();

            reservation.setRoom(optionalRoom.get());
            reservation.setUser(optionalUser.get());
            reservation.setCheckInDate(reservationDto.getCheckInDate());
            reservation.setCheckOutDate(reservationDto.getCheckOutDate());
            reservation.setReservationStatus(ReservationStatus.PENDING);

            Long days = ChronoUnit.DAYS.between(reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());
            reservation.setPrice(optionalRoom.get().getPrice()*days);

            reservationRepository.save(reservation);

            return true;
        }
        return false;
     }

     public ReservationResponse getAllReservationByUserId(Long userId, int numberPage){
         Pageable pageable = PageRequest.of(numberPage, SEARCH_PAGE);

         Page<Reservation> reservationPage = reservationRepository.findAllByUserId(pageable,userId);

         ReservationResponse reservationResponse = new ReservationResponse();

         reservationResponse.setReservationDtoList(reservationPage.stream()
                 .map(Reservation::getReservationDto).collect(Collectors.toList()));

         reservationResponse.setPageNumber(reservationPage.getPageable().getPageNumber());
         reservationResponse.setTotalPages(reservationResponse.getTotalPages());

         return reservationResponse;
     }
}
