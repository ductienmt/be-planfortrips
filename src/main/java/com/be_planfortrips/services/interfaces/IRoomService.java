package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.response.RoomResponse;

import java.util.Set;

public interface IRoomService {

    Set<RoomResponse> getAllRoom();

    RoomResponse getRoomById(Long roomId);

    RoomResponse createRoom(RoomDto roomDto);

    void deleteRoomById(Long roomId);

    RoomResponse updateRoomById(Long roomId, RoomDto roomDto);

}
