package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.BookingHotelResponse;
import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.entity.Room;
import com.be_planfortrips.entity.Payment;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
@Slf4j
public class BookingHotelMapper implements MapperInterface<BookingHotelResponse, BookingHotel, BookingHotelDto> {

    ModelMapper modelMapper;

    @Override
    public BookingHotel toEntity(BookingHotelDto bookingHotelDto) {
        BookingHotel bookingHotel = modelMapper.map(bookingHotelDto, BookingHotel.class);
//        bookingHotel.setUser(User.builder().userId(bookingHotelDto.getUserId()).build());
        bookingHotel.setUser(User.builder().id(bookingHotelDto.getUserId()).build());
        bookingHotel.setPayment(Payment.builder().id(bookingHotelDto.getPaymentId()).build());
        bookingHotel.setRoom(Room.builder().id(bookingHotelDto.getRoomId()).build());
        bookingHotel.setStatus(bookingHotelDto.getStatus());
        return  bookingHotel;
    }

    @Override
    public BookingHotelResponse toResponse(BookingHotel bookingHotel) {
        BookingHotelResponse response = modelMapper.map(bookingHotel, BookingHotelResponse.class);
//        response.setRoom(bookingHotel.getRoom());
        log.info(bookingHotel.getRoom().toString());
        return response;
    }

    @Override
    public void updateEntityFromDto(BookingHotelDto bookingHotelDto, BookingHotel bookingHotel) {


        modelMapper.map(bookingHotelDto, bookingHotel);
    }
}
