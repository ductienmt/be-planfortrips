package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.entity.Room;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
public class RoomMapper implements MapperInterface<RoomResponse, Room, RoomDto> {

    ModelMapper modelMapper;


    @Override
    public Room toEntity(RoomDto roomDto) {
        Room room = modelMapper.map(roomDto, Room.class);
        return room;
    }

    @Override
    public RoomResponse toResponse(Room room) {
        RoomResponse roomResponse = modelMapper.map(room, RoomResponse.class);
        return roomResponse;
    }

    @Override
    public void updateEntityFromDto(RoomDto roomDto, Room room) {

    }
}
