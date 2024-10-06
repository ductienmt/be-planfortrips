package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.entity.Room;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.mappers.impl.RoomMapper;
import com.be_planfortrips.repositories.RoomRepository;
import com.be_planfortrips.services.interfaces.IRoomService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class RoomServiceImpl implements IRoomService {

    RoomRepository roomRepository;
    MapperInterface<RoomResponse, Room, RoomDto> roomMapper;

    @Override
    public Set<RoomResponse> getAllRoom() {
        List<RoomResponse> responses = roomRepository.findAll().stream().map(roomMapper::toResponse).toList();
        return new HashSet<>(responses);
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        Room roomExist = roomRepository.findById(roomId).orElseThrow();
        return roomMapper.toResponse(roomExist);
    }

    @Override
    public RoomResponse createRoom(RoomDto roomDto) {
        Room roomCreate = roomMapper.toEntity(roomDto);
        return roomMapper.toResponse(roomRepository.save(roomCreate));
    }

    @Override
    public void deleteRoomById(Long roomId) {
        Room roomCreate = roomRepository.findById(roomId).orElseThrow();
        roomRepository.deleteById(roomId);
    }

    @Override
    public RoomResponse updateRoomById(Long roomId, RoomDto roomDto) {
        roomRepository.findById(roomId).orElseThrow();
        Room room = roomMapper.toEntity(roomDto);
        room.setId(roomId);
        return roomMapper.toResponse(roomRepository.save(room));
    }
}
