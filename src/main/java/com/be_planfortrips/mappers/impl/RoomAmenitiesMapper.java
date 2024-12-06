package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.HotelAmenitiesDto;
import com.be_planfortrips.dto.RoomAmenitiesDto;
import com.be_planfortrips.dto.response.RoomAmenitiesResponse;
import com.be_planfortrips.entity.HotelAmenities;
import com.be_planfortrips.entity.RoomAmenities;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.repositories.RoomRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class RoomAmenitiesMapper implements MapperInterface<RoomAmenitiesResponse, RoomAmenities, RoomAmenitiesDto> {
    ModelMapper modelMapper;
    RoomRepository roomRepository;


    @Override
    public RoomAmenities toEntity(RoomAmenitiesDto roomAmenitiesDto) {
//        System.out.println("RoomAmenitiesMapper toEntity" + roomAmenitiesDto.getFee());
        TypeMap<RoomAmenitiesDto, RoomAmenities> typeMap = modelMapper.createTypeMap(RoomAmenitiesDto.class, RoomAmenities.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(RoomAmenitiesDto.class, RoomAmenities.class);
            typeMap.addMappings(mapper -> mapper.skip(RoomAmenities::setId));
            typeMap.addMappings(mapper -> mapper.skip(RoomAmenities::setIcon));
        } else {
            typeMap.addMappings(mapper -> mapper.skip(RoomAmenities::setId));
            typeMap.addMappings(mapper -> mapper.skip(RoomAmenities::setIcon));
        }
        RoomAmenities roomAmenities = typeMap.map(roomAmenitiesDto);
        if (roomAmenitiesDto.getRoomId() != null) {
            roomRepository.findById(roomAmenitiesDto.getRoomId().longValue()).ifPresent(roomAmenities::setRoom);
        }
        return roomAmenities;
    }

    @Override
    public RoomAmenitiesResponse toResponse(RoomAmenities roomAmenities) {
        RoomAmenitiesResponse response = modelMapper.map(roomAmenities, RoomAmenitiesResponse.class);
        if (roomAmenities.getIcon() != null && roomAmenities.getIcon().getUrl() != null) {
            response.setIcon(roomAmenities.getIcon().getUrl());
//            System.out.println("RoomAmenitiesMapper toResponse" + roomAmenities.getIcon().getUrl());
        } else {
            response.setIcon(null);
        }
        return response;
    }

    @Override
    public void updateEntityFromDto(RoomAmenitiesDto roomAmenitiesDto, RoomAmenities roomAmenities) {
        for (Field dtoField : RoomAmenitiesDto.class.getDeclaredFields()) {
            try {
                if (java.lang.reflect.Modifier.isStatic(dtoField.getModifiers()) || java.lang.reflect.Modifier.isFinal(dtoField.getModifiers())) {
                    continue;
                }
                dtoField.setAccessible(true);
                Object newValue = dtoField.get(roomAmenitiesDto);

                if (newValue != null) {
                    Field entityField = RoomAmenities.class.getDeclaredField(dtoField.getName());
                    entityField.setAccessible(true);
                    Object currentValue = entityField.get(roomAmenities);

                    if (currentValue == null || !newValue.equals(currentValue)) {
                        entityField.set(roomAmenities, newValue);
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                log.error("Error while updating entity from dto", e);
                throw new AppException(ErrorType.inputFieldInvalid);
            }
        }
    }
}
