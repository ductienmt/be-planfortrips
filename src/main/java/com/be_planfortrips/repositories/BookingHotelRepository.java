package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.BookingHotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingHotelRepository extends JpaRepository<BookingHotel, Long> {
}
