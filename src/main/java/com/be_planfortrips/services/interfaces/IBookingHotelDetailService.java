package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.BookingHotelDetailResponse;

import java.util.List;
import java.util.UUID;

public interface IBookingHotelDetailService {

    List<BookingHotelDetailResponse> getAllBookingHotelDetail();

    BookingHotelDetailResponse getBookingHotelById(UUID bookingHotelDtlId);

}
