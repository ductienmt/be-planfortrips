package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.RoomDto;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.Room;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.repositories.HotelRepository;
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
    HotelRepository hotelRepository;
    private final RoomAmenitiesMapper roomAmenitiesMapper;


    @Override
    public Room toEntity(RoomDto roomDto) {
        Room room = modelMapper.map(roomDto, Room.class);
        Hotel hotel = hotelRepository.findById(roomDto.getHotelId()).orElseThrow(
                () -> new AppException(ErrorType.HotelIdNotFound)
        );
        room.setHotel(hotel);
        room.setAvailable(roomDto.getIsAvailable());
        return room;
    }

    @Override
    public RoomResponse toResponse(Room room) {
        RoomResponse roomResponse = modelMapper.map(room, RoomResponse.class);
        roomResponse.setImages(room.getImages().stream()
                .map(ri -> ri.getImage().getUrl())
                .toList());
        roomResponse.setHotelName(room.getHotel().getName());
        roomResponse.setRoomAmenities(room.getRoomAmenities().stream().map(roomAmenitiesMapper::toResponse).toList());
        return roomResponse;
    }

    @Override
    public void updateEntityFromDto(RoomDto roomDto, Room room) {

    }
}
