package com.hieunguyen.lakeSide.service;

import com.hieunguyen.lakeSide.dto.RoomDto;
import com.hieunguyen.lakeSide.model.Room;
import com.hieunguyen.lakeSide.repository.RoomRepository;
import com.hieunguyen.lakeSide.service.iml.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

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
}
