package com.hieunguyen.lakeSide.service.customer.room;

import com.hieunguyen.lakeSide.dto.response.RoomResponse;
import com.hieunguyen.lakeSide.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoomService {
    RoomResponse getAvailableRooms(int pageNumber);
}
