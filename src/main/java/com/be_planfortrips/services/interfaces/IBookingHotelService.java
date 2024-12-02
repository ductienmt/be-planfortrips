package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.BookingHotelDto;
import com.be_planfortrips.dto.response.BookingCustomer;
import com.be_planfortrips.dto.response.BookingHotelResponse;
import com.be_planfortrips.entity.BookingHotel;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IBookingHotelService {

    Set<BookingHotelResponse> getAllBookingHotel();

    BookingHotelResponse getBookingHotelByBookingId(Long bookingId);

    List<BookingHotelResponse> getBookingHotelByUserId(Long userId);

    BookingHotelResponse createBookingHotel(BookingHotelDto bookingHotelDto);

    BookingHotelResponse updateBookingHotel(BookingHotelDto bookingHotelDto, Long bookingHotelId);

    void deleteBookingHotelByBookingId(Long bookingId);

    List<BookingCustomer> findCustomersByEnterpriseId(String status);
}
