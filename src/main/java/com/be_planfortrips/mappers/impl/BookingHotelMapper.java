package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.BookingHotelDetailDto;
import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.BookingHotelResponse;
import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookingHotelMapper implements MapperInterface<BookingHotelResponse, BookingHotel, BookingHotelDto> {

   ModelMapper modelMapper;

    @Override
    public BookingHotel toEntity(BookingHotelDto bookingHotelDto) {
        BookingHotel hotel = modelMapper.map(bookingHotelDto, BookingHotel.class);
        return hotel;
    }

    @Override
    public BookingHotelResponse toResponse(BookingHotel bookingHotel) {
        BookingHotelResponse bookingHotelResponse = modelMapper.map(bookingHotel, BookingHotelResponse.class);
        return bookingHotelResponse;
    }

    @Override
    public void updateEntityFromDto(BookingHotelDto bookingHotelDto, BookingHotel bookingHotel) {

    }
}
