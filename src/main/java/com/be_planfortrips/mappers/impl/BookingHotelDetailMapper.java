package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.BookingHotelDetailDto;
import com.be_planfortrips.dto.response.BookingHotelDetailResponse;
import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.BookingHotelDetail;
import com.be_planfortrips.entity.Room;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.repositories.RoomRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookingHotelDetailMapper implements MapperInterface<BookingHotelDetailResponse, BookingHotelDetail, BookingHotelDetailDto> {

    ModelMapper modelMapper;
    RoomRepository roomRepository;

    @Override
    public BookingHotelDetail toEntity(BookingHotelDetailDto bookingHotelDetailDto) {
        BookingHotelDetail bookingHotelDetail = modelMapper.map(bookingHotelDetailDto, BookingHotelDetail.class);
        Long roomId = bookingHotelDetailDto.getRoomId();
        if (roomRepository.existsById(roomId)) {
            bookingHotelDetail.setRoom(Room.builder().build());
        }
        else {
            throw new AppException(ErrorType.roomIdNotFound, roomId);
        }
        return bookingHotelDetail;
    }

    @Override
    public BookingHotelDetailResponse toResponse(BookingHotelDetail bookingHotelDetail) {
        BookingHotelDetailResponse bookingHotelDetailResponse = modelMapper.map(bookingHotelDetail, BookingHotelDetailResponse.class);
        bookingHotelDetailResponse.setRoomId(bookingHotelDetail.getRoom().getId());
        bookingHotelDetailResponse.setUserId(bookingHotelDetail.getBookingHotel().getUser().getId());
        return bookingHotelDetailResponse;
    }

    @Override
    public void updateEntityFromDto(BookingHotelDetailDto bookingHotelDetailDto, BookingHotelDetail bookingHotelDetail) {

    }
}
