package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.response.BookingCustomer;
import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingHotelRepository extends JpaRepository<BookingHotel, Long> {
    List<BookingHotel> getBookingHotelByUser(User user);

    @Query(value = "SELECT bh FROM BookingHotel bh WHERE bh.bookingHotelId = :id")
    BookingHotel getBookingHotelById(Long id);

    @Query(value = "SELECT " +
            "bh.id AS bookingHotelId, " +
            "u.fullname AS customerName, " +
            "u.phonenumber AS customerPhone, " +
            "r.room_name AS roomName, " +
            "SUM(bhd.price) AS totalPrice, " +
            "r.type_of_room AS roomType, " +
            "bhd.check_in_time AS checkInDate, " +
            "bhd.check_out_time AS checkOutDate, " +
            "bh.status AS bookingStatus " +
            "FROM booking_hotels bh " +
            "LEFT JOIN users u ON bh.user_id = u.id " +
            "LEFT JOIN booking_hotels_details bhd ON bh.id = bhd.booking_id " +
            "LEFT JOIN rooms r ON bhd.room_id = r.id " +
            "LEFT JOIN hotels h ON r.hotel_id = h.id " +
            "WHERE h.enterprise_id = :enterpriseId " +
            "GROUP BY bh.id, u.fullname, u.phonenumber, r.type_of_room, r.room_name, bhd.check_in_time, bhd.check_out_time, bh.status " +
            "ORDER BY bhd.check_in_time DESC", nativeQuery = true)
    List<Object[]> findAllCustomersByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    @Query(value = "SELECT " +
            "bh.id AS bookingHotelId, " +
            "u.fullname AS customerName, " +
            "u.phonenumber AS customerPhone, " +
            "r.room_name AS roomName, " +
            "SUM(bhd.price) AS totalPrice, " +
            "r.type_of_room AS roomType, " +
            "bhd.check_in_time AS checkInDate, " +
            "bhd.check_out_time AS checkOutDate, " +
            "bh.status AS bookingStatus " +
            "FROM booking_hotels bh " +
            "LEFT JOIN users u ON bh.user_id = u.id " +
            "LEFT JOIN booking_hotels_details bhd ON bh.id = bhd.booking_id " +
            "LEFT JOIN rooms r ON bhd.room_id = r.id " +
            "LEFT JOIN hotels h ON r.hotel_id = h.id " +
            "WHERE h.enterprise_id = :enterpriseId " +
            "AND bhd.check_in_time > CURRENT_TIMESTAMP " +
            "GROUP BY bh.id, u.fullname, u.phonenumber, r.type_of_room, r.room_name, bhd.check_in_time, bhd.check_out_time, bh.status " +
            "ORDER BY bhd.check_in_time DESC", nativeQuery = true)
    List<Object[]> findCustomersByEnterpriseIdNotUse(@Param("enterpriseId") Long enterpriseId);

    @Query(value = "SELECT " +
            "bh.id AS bookingHotelId, " +
            "u.fullname AS customerName, " +
            "u.phonenumber AS customerPhone, " +
            "r.room_name AS roomName, " +
            "SUM(bhd.price) AS totalPrice, " +
            "r.type_of_room AS roomType, " +
            "bhd.check_in_time AS checkInDate, " +
            "bhd.check_out_time AS checkOutDate, " +
            "bh.status AS bookingStatus " +
            "FROM booking_hotels bh " +
            "LEFT JOIN users u ON bh.user_id = u.id " +
            "LEFT JOIN booking_hotels_details bhd ON bh.id = bhd.booking_id " +
            "LEFT JOIN rooms r ON bhd.room_id = r.id " +
            "LEFT JOIN hotels h ON r.hotel_id = h.id " +
            "WHERE h.enterprise_id = :enterpriseId " +
            "AND bhd.check_in_time <= CURRENT_TIMESTAMP AND bhd.check_out_time >= CURRENT_TIMESTAMP " +
            "GROUP BY bh.id, u.fullname, u.phonenumber, r.type_of_room, r.room_name, bhd.check_in_time, bhd.check_out_time, bh.status " +
            "ORDER BY bhd.check_in_time DESC", nativeQuery = true)
    List<Object[]> findCustomersByEnterpriseIdInUse(@Param("enterpriseId") Long enterpriseId);

    @Query(value = "SELECT " +
            "bh.id AS bookingHotelId, " +
            "u.fullname AS customerName, " +
            "u.phonenumber AS customerPhone, " +
            "r.room_name AS roomName, " +
            "SUM(bhd.price) AS totalPrice, " +
            "r.type_of_room AS roomType, " +
            "bhd.check_in_time AS checkInDate, " +
            "bhd.check_out_time AS checkOutDate, " +
            "bh.status AS bookingStatus " +
            "FROM booking_hotels bh " +
            "LEFT JOIN users u ON bh.user_id = u.id " +
            "LEFT JOIN booking_hotels_details bhd ON bh.id = bhd.booking_id " +
            "LEFT JOIN rooms r ON bhd.room_id = r.id " +
            "LEFT JOIN hotels h ON r.hotel_id = h.id " +
            "WHERE h.enterprise_id = :enterpriseId " +
            "AND bhd.check_out_time < CURRENT_TIMESTAMP " +
            "GROUP BY bh.id, u.fullname, u.phonenumber, r.type_of_room, r.room_name, bhd.check_in_time, bhd.check_out_time, bh.status " +
            "ORDER BY bhd.check_in_time DESC", nativeQuery = true)
    List<Object[]> findCustomersByEnterpriseused(@Param("enterpriseId") Long enterpriseId);
}