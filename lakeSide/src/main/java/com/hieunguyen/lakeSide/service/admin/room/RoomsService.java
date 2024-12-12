package com.hieunguyen.lakeSide.service.admin.room;

import com.hieunguyen.lakeSide.dto.RoomDto;
import com.hieunguyen.lakeSide.dto.response.RoomResponse;
import com.hieunguyen.lakeSide.model.Room;
import com.hieunguyen.lakeSide.repository.RoomRepository;
import com.hieunguyen.lakeSide.service.admin.room.iml.IRoomsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomsService implements IRoomsService {

    private final RoomRepository roomRepository;

    public boolean postRoom(RoomDto roomDto){
        try {
            Room room = new Room();

            room.setName(roomDto.getName());
            room.setType(roomDto.getType());
            room.setPrice(roomDto.getPrice());
            room.setAvailable(true);

            roomRepository.save(room);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public RoomResponse getAllPage( int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, 6);
        Page<Room> roomPage = roomRepository.findAll(pageable);

        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setPageNumber(roomPage.getNumber());
        roomResponse.setTotalPage(roomPage.getTotalPages());
        roomResponse.setRoomDtoList(roomPage.stream().map(Room::getRoomDto).collect(Collectors.toList()));

        return roomResponse;

    }

    public RoomDto getRoomById(Long id){

        Optional<Room> optionalRoom = roomRepository.findById(id);

        if (optionalRoom.isPresent()){
            return optionalRoom.get().getRoomDto();
        }else {
            throw  new EntityNotFoundException("Room not present");
        }
    }

    public boolean updateRoom(Long id, RoomDto roomDto){
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()){
            Room existRoom = optionalRoom.get();

            existRoom.setName(roomDto.getName());
            existRoom.setType(roomDto.getType());
            existRoom.setPrice(roomDto.getPrice());

            roomRepository.save(existRoom);

            return true;
        }else return false;
    }

    public void deleteRoom (Long id){
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()){
            roomRepository.deleteById(id);
        }else {
            throw  new EntityNotFoundException("Room not present");
        }
    }
}
