package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.BookingHotelDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingHotelDetailRepository extends JpaRepository<BookingHotelDetail, UUID> {
    List<BookingHotelDetail> findByBookingHotelBookingHotelId(Long bookingHotelId);

    boolean existsByRoomId(Long roomId);
}
