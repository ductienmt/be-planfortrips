package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.dto.response.HotelResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class HotelMapper implements MapperInterface<HotelResponse,Hotel,HotelDto> {
    ModelMapper modelMapper;
    @Override
    public Hotel toEntity(HotelDto hotelDto) {
        TypeMap<HotelDto, Hotel> typeMap = modelMapper.createTypeMap(hotelDto, Hotel.class);
        typeMap.addMappings(mapper -> mapper.skip(Hotel::setId));
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        return hotel;
    }

    @Override
    public HotelResponse toResponse(Hotel hotel) {
        HotelResponse hotelResponse = modelMapper.map(hotel, HotelResponse.class);
        return hotelResponse;
    }
    @Override
    public void updateEntityFromDto(HotelDto hotelDto, Hotel hotel) {
        modelMapper.map(hotelDto, hotel);
    }
}
