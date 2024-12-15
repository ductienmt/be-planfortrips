package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.sql.TourDataSql;
import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour,Integer> {
    @Query("select t from Tour t " +
            "inner join t.tags h " +
            "where (:titleName is null or :titleName = '' or t.title like %:titleName%) " +
            "and (:rating is null or t.rating <= :rating) " +
            "and (:tags is null or h.name in :tags) " +
            "and t.isActive = true")
    Page<Tour> searchTours(Pageable pageable,
                           @Param("titleName") String titleName,
                           @Param("rating") Integer rating,
                           @Param("tags") List<String> tags);
    @Query("select t from Tour t where t.isActive = true")
    Page<Tour> findAllByActive(PageRequest request);

    @Query(value = "WITH MatchedSchedules AS (\n" +
            "    SELECT\n" +
            "        s1.departure_time AS schedule1DepartureTime,\n" +
            "        s2.departure_time AS schedule2DepartureTime,\n" +
            "        s1.id AS schedule1Id,\n" +
            "        r1.id AS route1Id,\n" +
            "        s2.id AS schedule2Id,\n" +
            "        r2.id AS route2Id,\n" +
            "        hotels.id AS hotelId,\n" +
            "        ARRAY_AGG(DISTINCT ss1.id) AS scheduleSeatIds1,\n" +
            "        ARRAY_AGG(DISTINCT ss2.id) AS scheduleSeatIds2,\n" +
            "        ARRAY_AGG(DISTINCT rooms.id) AS hotelRoomIds,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY s1.id ORDER BY s2.departure_time ASC) AS rn\n" +
            "    FROM\n" +
            "        schedules s1\n" +
            "            JOIN schedules s2\n" +
            "                 ON s1.departure_time < s2.departure_time\n" +
            "            JOIN routes r1\n" +
            "                 ON s1.route_id = r1.id\n" +
            "            JOIN routes r2\n" +
            "                 ON s2.route_id = r2.id\n" +
            "            JOIN schedule_seats ss1\n" +
            "                 ON s1.id = ss1.schedule_id AND ss1.status = 'Empty'\n" +
            "            JOIN schedule_seats ss2\n" +
            "                 ON s2.id = ss2.schedule_id AND ss2.status = 'Empty'\n" +
            "            JOIN seats st1\n" +
            "                 ON st1.id = ss1.seat_id\n" +
            "            JOIN seats st2\n" +
            "                 ON st2.id = ss2.seat_id\n" +
            "            JOIN vehicles vh1\n" +
            "                 ON vh1.code = st1.vehicle_code\n" +
            "            JOIN vehicles vh2\n" +
            "                 ON vh2.code = st2.vehicle_code\n" +
            "            JOIN car_company cc1\n" +
            "                 ON cc1.id = vh1.car_company_id AND cc1.id = :carCompanyId\n" +
            "            JOIN car_company cc2\n" +
            "                 ON cc2.id = vh2.car_company_id AND cc2.id = :carCompanyId\n" +
            "            JOIN hotels\n" +
            "                 ON hotels.id = :hotelId\n" +
            "            JOIN rooms\n" +
            "                 ON rooms.hotel_id = hotels.id\n" +
            "            LEFT JOIN booking_hotels_details bhd\n" +
            "                      ON bhd.room_id = rooms.id\n" +
            "                          AND bhd.check_in_time < s2.departure_time\n" +
            "                          AND bhd.check_out_time > s1.arrival_time\n" +
            "    WHERE\n" +
            "        r1.id = :routeId\n" +
            "      AND r1.destination_station_id = r2.origin_station_id\n" +
            "      AND r1.origin_station_id = r2.destination_station_id\n" +
            "      AND NOT EXISTS (\n" +
            "        SELECT 1\n" +
            "        FROM booking_hotels_details bhd_inner\n" +
            "        WHERE bhd_inner.room_id = rooms.id\n" +
            "          AND (\n" +
            "            (bhd_inner.check_in_time < s2.departure_time\n" +
            "                AND bhd_inner.check_out_time > s1.arrival_time)\n" +
            "                OR\n" +
            "            (bhd_inner.check_out_time > s1.arrival_time)\n" +
            "            )\n" +
            "    )\n" +
            "    GROUP BY\n" +
            "        s1.id, r1.id, s2.id, r2.id, hotels.id\n" +
            "    HAVING\n" +
            "        COUNT(DISTINCT ss1.id) >= :numberOfPeople\n" +
            "       AND COUNT(DISTINCT ss2.id) >= :numberOfPeople\n" +
            "       AND (\n" +
            "               SELECT SUM(r.max_size)\n" +
            "               FROM rooms r\n" +
            "               WHERE r.hotel_id = hotels.id\n" +
            "           ) >= :numberOfPeople\n" +
            ")\n" +
            "SELECT *\n" +
            "FROM MatchedSchedules\n" +
            "WHERE rn = 1;\n", nativeQuery = true)
    List<TourDataSql> getResourceAvailable(@Param("routeId") String routeId,
                                           @Param("hotelId") Long hotelId,
                                           @Param("carCompanyId") Integer carCompanyId,
                                           @Param("numberOfPeople") int numberOfPeople);

    @Query("SELECT t FROM Tour t " +
            "JOIN t.route r " +
            "JOIN r.destinationStation stDes " +
            "JOIN r.originStation stOrigin " +
            "JOIN stDes.city cDes " +
            "JOIN stOrigin.city cOrigin " +
            "WHERE cDes.id = :cityDesId AND cOrigin.id = :cityOriginId")
    List<Tour> getTourByCityId(@Param("cityDesId") String cityDesId, @Param("cityOriginId") String cityOriginId);

     @Query(value = "SELECT tours.*, COUNT(tours_user_used.user_used_id) AS user_count " +
            "FROM tours " +
            "JOIN tours_user_used ON tours.id = tours_user_used.tour_id " +
            "GROUP BY tours.id " +
            "ORDER BY user_count DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Tour> getTourTop1Used();

    @Query("select t from Tour t join t.route r on t.route.id = r.id " +
            "join r.destinationStation s on r.destinationStation.id = s.id " +
            "join s.city c on s.city.id = c.id " +
            "where c.id = :cityId")
    List<Tour> getTourHasCityDestination(@Param("cityId") String cityId);

    @Query(value = "select * from tours\n" +
            "join tour_checkins on tours.id = tour_checkins.tour_id\n" +
            "and tour_checkins.checkin_id = 553", nativeQuery = true)
    List<Tour> getTourHasCheckIn(@Param("checkInId") Integer checkInId);


    @Query("select t from Tour t join t.route r on t.route.id = r.id " +
            "join r.destinationStation s on r.destinationStation.id = s.id " +
            "join s.city c on s.city.id = c.id " +
            "where c.area.id = :areaId")
    List<Tour> getTourHaveCityIn(@Param("areaId") String areaId);


}
