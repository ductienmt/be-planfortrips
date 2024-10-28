package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingHotelRepository extends JpaRepository<BookingHotel, Long> {
    List<BookingHotel> getBookingHotelByUser(User user);

    @Query(value = "SELECT bh FROM BookingHotel bh WHERE bh.bookingHotelId = :id")
    BookingHotel getBookingHotelById(Long id);
}
