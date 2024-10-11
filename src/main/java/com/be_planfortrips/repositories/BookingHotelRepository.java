package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingHotelRepository extends JpaRepository<BookingHotel, Long> {

    List<BookingHotel> getBookingHotelByUser(User user);
}
