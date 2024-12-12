package com.hieunguyen.lakeSide.dto.response;

import com.hieunguyen.lakeSide.dto.RoomDto;
import lombok.Data;

import java.util.List;

@Data
public class RoomResponse {

    private List<RoomDto> roomDtoList;

    private Integer totalPage;

    private Integer pageNumber;
}
