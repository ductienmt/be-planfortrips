package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.BookingHotelResponse;

import java.util.Set;

public interface IBookingHotelService {

    Set<BookingHotelResponse> getAllBookingHotel();

    BookingHotelResponse getBookingHotelById(Long bookingHotelId);

    BookingHotelResponse createBookingHotel(BookingHotelDto bookingHotelDto);

    void deleteBookingHotelById(Long bookingHotelId);

    BookingHotelResponse updateBookingHotel(Long bookingHotelId, BookingHotelDto bookingHotelDto);

}
