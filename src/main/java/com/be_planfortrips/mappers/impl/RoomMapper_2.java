package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.response.RoomResponseEnterprise;
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
public class RoomMapper_2 implements MapperInterface<RoomResponseEnterprise, Room, RoomDto> {

    ModelMapper modelMapper;

    @Override
    public Room toEntity(RoomDto roomDto) {
        return null;
    }

    @Override
    public RoomResponseEnterprise toResponse(Room room) {
        return modelMapper.map(room, RoomResponseEnterprise.class);
    }

    @Override
    public void updateEntityFromDto(RoomDto roomDto, Room room) {

    }
}
