package com.be_planfortrips.services;

import com.be_planfortrips.dto.sql.StatisticalBookingHotelDetail;
import com.be_planfortrips.repositories.BookingHotelDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticalService {

    BookingHotelDetailRepository bookingHotelDetailRepository;

    // Thống kê tháng có booking nhiều nhất trong năm
     public List<StatisticalBookingHotelDetail> statisticalBookingHotelByYear(Integer year) {
         return bookingHotelDetailRepository.StatisticalBookingHotelByYear(year);
     };
}
