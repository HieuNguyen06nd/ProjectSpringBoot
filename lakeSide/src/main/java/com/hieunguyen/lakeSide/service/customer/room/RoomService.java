package com.hieunguyen.lakeSide.service.customer.room;

import com.hieunguyen.lakeSide.dto.response.RoomResponse;
import com.hieunguyen.lakeSide.model.Room;
import com.hieunguyen.lakeSide.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService{
    private final RoomRepository roomRepository;

    public RoomResponse getAvailableRooms(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, 6);
        Page<Room> roomPage = roomRepository.findByAvailable(true,pageable);

        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setPageNumber(roomPage.getNumber());
        roomResponse.setTotalPage(roomPage.getTotalPages());
        roomResponse.setRoomDtoList(roomPage.stream().map(Room::getRoomDto).collect(Collectors.toList()));

        return roomResponse;

    }
}
