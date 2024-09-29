package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.dto.HotelImageDto;
import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.HotelImage;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.responses.HotelImageResponse;
import com.be_planfortrips.responses.HotelResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class HotelImageMapper implements MapperInterface<HotelImageResponse, HotelImage, HotelImageDto> {
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public HotelImage toEntity(HotelImageDto hotelImageDto) {
        return null;
    }
    @Override
    public HotelImageResponse toResponse(HotelImage hotelImage) {
        return HotelImageResponse.builder()
                .hotelId(hotelImage.getHotel().getId())
                .url(hotelImage.getImage().getUrl())
                .build();
    }

    @Override
    public void updateEntityFromDto(HotelImageDto hotelImageDto, HotelImage hotelImage) {

    }
}
