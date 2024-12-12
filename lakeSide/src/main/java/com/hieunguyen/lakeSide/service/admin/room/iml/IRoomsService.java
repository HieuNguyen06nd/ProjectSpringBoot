package com.hieunguyen.lakeSide.service.admin.room.iml;

import com.hieunguyen.lakeSide.dto.RoomDto;
import com.hieunguyen.lakeSide.dto.response.RoomResponse;

public interface IRoomsService {
    boolean postRoom(RoomDto roomDto);

    RoomResponse getAllPage(int pageNumber);

    RoomDto getRoomById(Long id);

    boolean updateRoom(Long id, RoomDto roomDto);

    void deleteRoom (Long id);
}
