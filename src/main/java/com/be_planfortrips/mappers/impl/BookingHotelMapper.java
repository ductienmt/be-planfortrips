package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.BookingHotelDetailDto;
import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.BookingHotelResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.repositories.PaymentRepository;
import com.be_planfortrips.repositories.RoomRepository;
import com.be_planfortrips.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookingHotelMapper implements MapperInterface<BookingHotelResponse, BookingHotel, BookingHotelDto> {

   ModelMapper modelMapper;
   RoomRepository roomRepository;
   UserRepository userRepository;
   PaymentRepository paymentRepository;

    @Override
    public BookingHotel toEntity(BookingHotelDto bookingHotelDto) {
        BookingHotel hotel = modelMapper.map(bookingHotelDto, BookingHotel.class);
        Long paymentId = bookingHotelDto.getPaymentId();

        if (!paymentRepository.existsById(paymentId)) {
            throw new AppException(ErrorType.paymentIdNotFound, paymentId);
        }

        List<BookingHotelDetail> bookingHotelDetails = new ArrayList<>();

        hotel.setTotalPrice(bookingHotelDto.getTotalPrice());
        bookingHotelDto.getBookingHotelDetailDto().forEach((detailDto) -> {
            Long roomId = detailDto.getRoomId();
            Room room = roomRepository.findById(roomId).orElseThrow(
                    () -> new AppException(ErrorType.roomIdNotFound, roomId)
            );
                BookingHotelDetail detail = modelMapper.map(detailDto, BookingHotelDetail.class);
                detail.setRoom(Room.builder().id(roomId).build());
                detail.setBookingHotel(hotel);
                detail.setPrice(detailDto.getPrice());
                // Giá tại thời điểm hiện tại (Tránh trường hợp tăng giá -> Select Sai giá cũ)
                detail.setPrice(room.getPrice());
                bookingHotelDetails.add(detail);
        });

        hotel.setPayment(Payment.builder().id(paymentId).build());
        hotel.setBookingHotelDetails(new HashSet<>(bookingHotelDetails));
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
