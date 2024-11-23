package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.HotelAmenitiesDto;
import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.response.HotelAmenitiesResponse;
import com.be_planfortrips.entity.HotelAmenities;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.repositories.HotelRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
@Slf4j
public class HotelAmenitiesMapper implements MapperInterface<HotelAmenitiesResponse, HotelAmenities, HotelAmenitiesDto> {
    ModelMapper modelMapper;
    private final HotelRepository hotelRepository;

    @Override
    public HotelAmenities toEntity(HotelAmenitiesDto hotelAmenitiesDto) {
        TypeMap<HotelAmenitiesDto, HotelAmenities> typeMap = modelMapper.createTypeMap(HotelAmenitiesDto.class, HotelAmenities.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(HotelAmenitiesDto.class, HotelAmenities.class);
            typeMap.addMappings(mapper -> mapper.skip(HotelAmenities::setId));
            typeMap.addMappings(mapper -> mapper.skip(HotelAmenities::setIcon));
        } else {
            typeMap.addMappings(mapper -> mapper.skip(HotelAmenities::setId));
            typeMap.addMappings(mapper -> mapper.skip(HotelAmenities::setIcon));
        }
        HotelAmenities hotelAmenities = typeMap.map(hotelAmenitiesDto);
        if (hotelAmenitiesDto.getHotelId() != null) {
            hotelRepository.findById(hotelAmenitiesDto.getHotelId()).ifPresent(hotelAmenities::setHotel);
        }
        return hotelAmenities;
    }

    @Override
    public HotelAmenitiesResponse toResponse(HotelAmenities hotelAmenities) {
        HotelAmenitiesResponse response = modelMapper.map(hotelAmenities, HotelAmenitiesResponse.class);
        if (hotelAmenities.getIcon() != null && hotelAmenities.getIcon().getUrl() != null) {
            response.setIcon(hotelAmenities.getIcon().getUrl());
        } else {
            response.setIcon(null);
        }
        return response;
    }

    @Override
    public void updateEntityFromDto(HotelAmenitiesDto hotelAmenitiesDto, HotelAmenities hotelAmenities) {
        for (Field dtoField : HotelAmenitiesDto.class.getDeclaredFields()) {
            try {
                if (java.lang.reflect.Modifier.isStatic(dtoField.getModifiers()) || java.lang.reflect.Modifier.isFinal(dtoField.getModifiers())) {
                    continue;
                }
                dtoField.setAccessible(true);
                Object newValue = dtoField.get(hotelAmenitiesDto);

                if (newValue != null) {
                    Field entityField = HotelAmenities.class.getDeclaredField(dtoField.getName());
                    entityField.setAccessible(true);
                    Object currentValue = entityField.get(hotelAmenities);

                    if (currentValue == null || !newValue.equals(currentValue)) {
                        entityField.set(hotelAmenities, newValue);
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                log.error("Error while updating entity from dto", e);
                throw new AppException(ErrorType.inputFieldInvalid);
            }
        }
    }

}
