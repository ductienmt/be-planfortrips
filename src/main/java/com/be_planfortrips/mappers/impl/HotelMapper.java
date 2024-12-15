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
    private final HotelAmenitiesMapper hotelAmenitiesMapper;

    @Override
    public Hotel toEntity(HotelDto hotelDto) {
        TypeMap<HotelDto, Hotel> typeMap = modelMapper.getTypeMap(HotelDto.class, Hotel.class);

        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(HotelDto.class, Hotel.class);
            typeMap.addMappings(mapper -> mapper.skip(Hotel::setId));
            typeMap.addMappings(mapper -> mapper.skip(Hotel::setRooms));
            typeMap.addMappings(mapper -> mapper.skip(Hotel::setHotelAmenities));
        }
        Hotel Hotel = modelMapper.map(hotelDto, Hotel.class);
        return Hotel;
    }

    @Override
    public HotelResponse toResponse(Hotel hotel) {
        HotelResponse hotelResponse = modelMapper.map(hotel, HotelResponse.class);
        hotelResponse.setRooms(hotel.getRooms());
        hotelResponse.setEnterpriseId(hotel.getAccountEnterprise().getAccountEnterpriseId());
        if (hotel.getHotelAmenities() != null) {
            hotelResponse.setHotelAmenities(hotel.getHotelAmenities().stream().map(hotelAmenitiesMapper::toResponse).collect(Collectors.toList()));
        } else {
            hotelResponse.setHotelAmenities(Collections.emptyList());
        }
        return hotelResponse;
    }
    @Override
    public void updateEntityFromDto(HotelDto hotelDto, Hotel hotel) {
        modelMapper.map(hotelDto, hotel);
    }
}
