package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.sql.StatisticalBookingHotelDetail;
import com.be_planfortrips.entity.BookingHotelDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BookingHotelDetailRepository extends JpaRepository<BookingHotelDetail, UUID> {
    List<BookingHotelDetail> findByBookingHotelBookingHotelId(Long bookingHotelId);

    boolean existsByRoomId(Long roomId);

    @Query(value = "WITH months AS (\n" +
            "    SELECT\n" +
            "        GENERATE_SERIES(\n" +
            "                DATE_TRUNC('year', TO_DATE(:year || '-01-01', 'YYYY-MM-DD')),\n" +
            "                DATE_TRUNC('year', TO_DATE(:year || '-01-01', 'YYYY-MM-DD')) + INTERVAL '11 months',\n" +
            "                INTERVAL '1 month'\n" +
            "        ) AS month\n" +
            ")\n" +
            "SELECT\n" +
            "    TO_CHAR(months.month, 'YYYY-MM') AS month,\n" +
            "    COALESCE(COUNT(booking_hotels_details.booking_hotel_detail_id), 0) AS total_details\n" +
            "FROM\n" +
            "    months\n" +
            "        LEFT JOIN\n" +
            "    booking_hotels_details\n" +
            "    ON\n" +
            "        DATE_TRUNC('month', booking_hotels_details.create_at) = months.month\n" +
            "GROUP BY\n" +
            "    months.month\n" +
            "ORDER BY\n" +
            "    months.month;\n",nativeQuery = true)
    List<StatisticalBookingHotelDetail> StatisticalBookingHotelByYear(Integer year);
}
