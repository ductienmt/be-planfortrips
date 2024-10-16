package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.BookingHotelDetailDto;
import com.be_planfortrips.dto.response.BookingHotelDetailResponse;
import com.be_planfortrips.entity.BookingHotelDetail;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.repositories.BookingHotelDetailRepository;
import com.be_planfortrips.services.interfaces.IBookingHotelDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BookingHotelDetailServiceImpl implements IBookingHotelDetailService {

    BookingHotelDetailRepository bookingHotelDetailRepository;
    MapperInterface<BookingHotelDetailResponse, BookingHotelDetail, BookingHotelDetailDto> bookingHotelDtlMapper;


    @Override
    public List<BookingHotelDetailResponse> getAllBookingHotelDetail() {
        return bookingHotelDetailRepository.findAll().
                stream().map(bookingHotelDtlMapper::toResponse).toList();
    }

    @Override
    public BookingHotelDetailResponse getBookingHotelById(UUID bookingHotelDtlId) {
        BookingHotelDetail bookingHotelDetail =
                bookingHotelDetailRepository.findById(bookingHotelDtlId).orElseThrow(
                        () -> new AppException(ErrorType.notFound)
                );
        return bookingHotelDtlMapper.toResponse(bookingHotelDetail);
    }
}
