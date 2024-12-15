package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.response.HotelResponses.AvailableHotels;
import com.be_planfortrips.dto.sql.StatisticalResource;
import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.HotelAmenities;
import com.be_planfortrips.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select h from Hotel h " +
            " where (:keyword is null or :keyword = '' or h.name ilike %:keyword% or h.address ilike %:keyword%  " +
            "and (:rating is null or h.rating <= :rating))")
    Page<Hotel> searchHotels(Pageable pageable,@Param("keyword") String keyword,@Param("rating") Integer rating);
    List<Hotel> searchByNameContains(String name);
    @Query("select h from Hotel h where h.accountEnterprise.accountEnterpriseId = :enterpriseId")
    List<Hotel> findByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    @Query("select h from Hotel h inner join AccountEnterprise ae " +
            "ON ae.accountEnterpriseId = h.accountEnterprise.accountEnterpriseId \n" +
            "inner join City c ON c.id = ae.city.id \n" +
            "inner join Station s ON s.city.id = c.id \n" +
            "INNER JOIN Route r ON r.originStation.id = s.id OR r.destinationStation.id = s.id \n" +
            "where r.id = :route_id")
    List<Hotel> findHotelByRouteId(@Param("route_id") String routeId);
    @Query("SELECT h " +
            "FROM Hotel h " +
            "INNER JOIN Room r ON h.id = r.hotel.id " +
            "WHERE (:destination IS NULL OR trim(:destination) = '' " +
            "   OR lower(r.hotel.accountEnterprise.city.nameCity) LIKE lower(concat('%', :destination, '%')) " +
            "   OR lower(h.name) LIKE lower(concat('%', :destination, '%')) " +
            "   OR lower(h.address) LIKE lower(concat('%', :destination, '%'))) " +
            "AND r.id NOT IN (" +
            "  SELECT b.room.id FROM BookingHotelDetail b " +
            "  WHERE b.checkInTime < :checkOutDate AND b.checkOutTime > :checkInDate" +
            ")")
    Page<Hotel> findAvailableHotels(Pageable pageable,
                                    @Param("checkInDate") LocalDateTime checkInDate,
                                    @Param("checkOutDate") LocalDateTime checkOutDate,
                                    @Param("destination") String destination);

    @Query(value = "SELECT h, r " +
            "FROM Hotel h " +
            "JOIN h.rooms r " +
            "WHERE r.price BETWEEN :minPrice AND :maxPrice " +
            "AND h.accountEnterprise.city.nameCity ilike concat('%', :city, '%') ")
    List<Object[]> findHotelsWithRoomsInPriceRange(@Param("minPrice") BigDecimal minPrice,
                                                   @Param("maxPrice") BigDecimal maxPrice,
                                                   @Param("city") String city);
    @Query("select h FROM Hotel h " +
            "join h.rooms r " +
            "where  r.id = :roomId")
    Hotel getHotelByRoomId(@Param("roomId") Long id);


    @Query(value = "WITH all_months AS (\n" +
            "    SELECT generate_series(1, 12) AS month\n" +
            "),\n" +
            "     room_bookings AS (\n" +
            "         SELECT r.hotel_id,\n" +
            "                EXTRACT(MONTH FROM bhd.create_at) AS month,\n" +
            "                COUNT(bhd.booking_hotel_detail_id) AS booking_count\n" +
            "         FROM rooms r\n" +
            "                  LEFT JOIN booking_hotels_details bhd ON r.id = bhd.room_id\n" +
            "         WHERE EXTRACT(YEAR FROM bhd.create_at) = :year\n" +
            "         GROUP BY r.hotel_id, EXTRACT(MONTH FROM bhd.create_at)\n" +
            "     ),\n" +
            "     ranked_rooms AS (\n" +
            "         SELECT\n" +
            "             rb.month,\n" +
            "             rb.hotel_id,\n" +
            "             rb.booking_count,\n" +
            "             RANK() OVER (PARTITION BY rb.month ORDER BY rb.booking_count DESC) AS rank\n" +
            "         FROM room_bookings rb\n" +
            "     )\n" +
            "SELECT\n" +
            "    am.month AS month,\n" +
            "    rr.hotel_id AS resource_id,\n" +
            "    COALESCE(rr.booking_count, 0) AS count\n" +
            "FROM all_months am\n" +
            "         LEFT JOIN ranked_rooms rr ON am.month = rr.month AND rr.rank = 1\n" +
            "ORDER BY am.month;\n", nativeQuery = true)
    List<StatisticalResource> getTop1HotelByYear(@Param("year") int year);

    @Query("select h from Hotel h where h.accountEnterprise.accountEnterpriseId = :id " +
            "and (:keyword is null or :keyword = '' or h.name ilike %:keyword% " +
            "or h.address ilike %:keyword% or h.phoneNumber ilike %:keyword% " +
            "or h.description ilike %:keyword%)")
    Page<Hotel> searchEnterprise(@Param("keyword") String keyword, @Param("id") Long id, Pageable pageable);


}
