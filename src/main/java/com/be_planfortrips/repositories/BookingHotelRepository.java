package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.response.BookingCustomer;
import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

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

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    TO_CHAR(DATE_TRUNC('week', b.create_at), 'DD-MM-YYYY') AS week_start,\n" +
            "    SUM(d.price) AS total_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE TO_CHAR(b.create_at, 'D')::INTEGER = 2) AS monday_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE TO_CHAR(b.create_at, 'D')::INTEGER = 3) AS tuesday_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE TO_CHAR(b.create_at, 'D')::INTEGER = 4) AS wednesday_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE TO_CHAR(b.create_at, 'D')::INTEGER = 5) AS thursday_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE TO_CHAR(b.create_at, 'D')::INTEGER = 6) AS friday_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE TO_CHAR(b.create_at, 'D')::INTEGER = 7) AS saturday_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE TO_CHAR(b.create_at, 'D')::INTEGER = 1) AS sunday_revenue\n" +
            "FROM\n" +
            "    booking_hotels b\n" +
            "        JOIN\n" +
            "    booking_hotels_details d ON b.id = d.booking_id\n" +
            "join rooms r on d.room_id = r.id\n" +
            "join hotels h on r.hotel_id = h.id\n" +
            "WHERE\n" +
            "    h.enterprise_id = :enterpriseId\n" +
            "  AND b.status = 'Complete'\n" +
            "GROUP BY\n" +
            "    DATE_TRUNC('week', b.create_at);")
    List<Map<String, Object>> compileRevenueWithWeek(@Param("enterpriseId") Long enterpriseId);


    @Query(nativeQuery = true, value = "SELECT\n" +
            "    TO_CHAR(DATE_TRUNC('month', b.create_at), 'DD-MM-YYYY') AS month_start,\n" +
            "    SUM(d.price) AS total_revenue,\n" +
            "    SUM(d.price) FILTER (\n" +
            "        WHERE EXTRACT(WEEK FROM b.create_at) = EXTRACT(WEEK FROM DATE_TRUNC('month', b.create_at)) + 0\n" +
            "        ) AS week_1_revenue,\n" +
            "    SUM(d.price) FILTER (\n" +
            "        WHERE EXTRACT(WEEK FROM b.create_at) = EXTRACT(WEEK FROM DATE_TRUNC('month', b.create_at)) + 1\n" +
            "        ) AS week_2_revenue,\n" +
            "    SUM(d.price) FILTER (\n" +
            "        WHERE EXTRACT(WEEK FROM b.create_at) = EXTRACT(WEEK FROM DATE_TRUNC('month', b.create_at)) + 2\n" +
            "        ) AS week_3_revenue,\n" +
            "    SUM(d.price) FILTER (\n" +
            "        WHERE EXTRACT(WEEK FROM b.create_at) = EXTRACT(WEEK FROM DATE_TRUNC('month', b.create_at)) + 3\n" +
            "        ) AS week_4_revenue\n" +
            "FROM\n" +
            "    booking_hotels b\n" +
            "        JOIN\n" +
            "    booking_hotels_details d ON b.id = d.booking_id\n" +
            "        JOIN\n" +
            "    rooms r ON d.room_id = r.id\n" +
            "        JOIN\n" +
            "    hotels h ON r.hotel_id = h.id\n" +
            "WHERE\n" +
            "    h.enterprise_id = :enterpriseId\n" +
            "  AND b.status = 'Complete'\n" +
            "GROUP BY\n" +
            "    DATE_TRUNC('month', b.create_at)\n" +
            "ORDER BY\n" +
            "    month_start;")
    List<Map<String, Object>> compileRevenueWithMonth(@Param("enterpriseId") Long enterpriseId);

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    TO_CHAR(DATE_TRUNC('year', b.create_at), 'DD-MM-YYYY') AS year_start,\n" +
            "    SUM(d.price) AS total_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 1) AS january_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 2) AS february_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 3) AS march_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 4) AS april_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 5) AS may_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 6) AS june_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 7) AS july_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 8) AS august_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 9) AS september_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 10) AS october_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 11) AS november_revenue,\n" +
            "    SUM(d.price) FILTER (WHERE EXTRACT(MONTH FROM b.create_at) = 12) AS december_revenue\n" +
            "FROM\n" +
            "    booking_hotels b\n" +
            "        JOIN\n" +
            "    booking_hotels_details d ON b.id = d.booking_id\n" +
            "        JOIN\n" +
            "    rooms r ON d.room_id = r.id\n" +
            "        JOIN\n" +
            "    hotels h ON r.hotel_id = h.id\n" +
            "WHERE\n" +
            "    h.enterprise_id = :enterpriseId\n" +
            "  AND b.status = 'Complete'\n" +
            "GROUP BY\n" +
            "    DATE_TRUNC('year', b.create_at)\n" +
            "ORDER BY\n" +
            "    year_start;\n")
    List<Map<String, Object>> compileRevenueWithYear(@Param("enterpriseId") Long enterpriseId);

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    count(r.id) AS available_rooms_count\n" +
            "FROM\n" +
            "    rooms r\n" +
            "        LEFT JOIN booking_hotels_details d ON r.id = d.room_id\n" +
            "        JOIN hotels h ON r.hotel_id = h.id\n" +
            "WHERE\n" +
            "    h.enterprise_id = :enterpriseId  \n" +
            "  AND r.is_available = TRUE \n" +
            "  AND (\n" +
            "    d.room_id IS NULL \n" +
            "    OR (\n" +
            "        d.check_in_time > NOW()  \n" +
            "        OR d.check_out_time < NOW()\n" +
            "    )\n" +
            ");")
    Integer countRoomNotUse(@Param("enterpriseId") Long enterpriseId);

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    count(r.id) AS unavailable_rooms_count\n" +
            "FROM\n" +
            "    rooms r\n" +
            "        JOIN booking_hotels_details d ON r.id = d.room_id\n" +
            "        JOIN booking_hotels b ON b.id = d.booking_id\n" +
            "        JOIN hotels h ON r.hotel_id = h.id\n" +
            "WHERE\n" +
            "    h.enterprise_id = :enterpriseId\n" +
            "  AND r.is_available = TRUE\n" +
            "  AND d.check_in_time <= NOW()\n" +
            "  AND d.check_out_time >= NOW();")
    Integer countRoomInUse(Long enterpriseId);

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    count(r.id) AS room_used_count\n" +
            "FROM\n" +
            "    rooms r\n" +
            "        JOIN booking_hotels_details d ON r.id = d.room_id\n" +
            "        JOIN booking_hotels b ON b.id = d.booking_id\n" +
            "        JOIN hotels h ON r.hotel_id = h.id\n" +
            "WHERE\n" +
            "    h.enterprise_id = :enterpriseId\n" +
            "  AND r.is_available = TRUE\n" +
            "  AND d.check_out_time < NOW();\n")
    Integer countRoomUsed(@Param("enterpriseId") Long enterpriseId);

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    count(r.id) AS total_rooms\n" +
            "FROM\n" +
            "    rooms r\n" +
            "        JOIN booking_hotels_details d ON r.id = d.room_id\n" +
            "        JOIN booking_hotels b ON b.id = d.booking_id\n" +
            "        JOIN hotels h ON r.hotel_id = h.id\n" +
            "WHERE\n" +
            "    h.enterprise_id = :enterpriseId\n" +
            "  AND r.is_available = TRUE\n" +
            "  AND d.check_in_time >= NOW();\n")
    Integer countRoomFuture(@Param("enterpriseId") Long enterpriseId);
}