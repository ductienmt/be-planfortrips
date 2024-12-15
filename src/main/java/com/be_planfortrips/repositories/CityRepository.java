package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CityRepository extends JpaRepository<City, String> {
    List<City> findByArea_Id(String area_id);

    @Query("SELECT c.id, c.nameCity, c.description FROM City c")
    List<Object[]> getAllCities();
    List<City> searchByNameCityContaining(String nameCity);

    @Query(nativeQuery = true, value = "SELECT c.id AS city_id,\n" +
            "       c.name_city,\n" +
            "       i.url AS image_url,\n" +
            "       SUM(counts.total_count) AS total_count\n" +
            "FROM cities c\n" +
            "         INNER JOIN (\n" +
            "\n" +
            "    SELECT p.destination AS city_id, COUNT(*) AS total_count\n" +
            "    FROM plan p\n" +
            "    WHERE p.destination IS NOT NULL\n" +
            "    GROUP BY p.destination\n" +
            "\n" +
            "    UNION ALL\n" +
            "\n" +
            "    SELECT s.city_id AS city_id, COUNT(t.id) AS total_count\n" +
            "    FROM tickets t\n" +
            "             JOIN schedules sc ON t.schedule_id = sc.id\n" +
            "             JOIN routes r ON sc.route_id = r.id\n" +
            "             JOIN stations s ON r.destination_station_id = s.id\n" +
            "    WHERE s.city_id IS NOT NULL\n" +
            "    GROUP BY s.city_id\n" +
            "\n" +
            "    UNION ALL\n" +
            "\n" +
            "    SELECT ae.city_id AS city_id, COUNT(bh.id) AS total_count\n" +
            "    FROM booking_hotels bh\n" +
            "             JOIN hotels h ON bh.id = h.id\n" +
            "             JOIN account_enterprises ae ON h.enterprise_id = ae.id\n" +
            "    WHERE ae.city_id IS NOT NULL\n" +
            "    GROUP BY ae.city_id\n" +
            ") counts ON c.id = counts.city_id\n" +
            "         LEFT JOIN images i ON c.image_id = i.id\n" +
            "GROUP BY c.id, c.name_city, i.url\n" +
            "ORDER BY total_count DESC\n" +
            "LIMIT 10;\n")
    List<Map<String, Object>> getFavoriteCity();

    @Query(nativeQuery = true, value = "WITH CityTourCount AS (\n" +
            "    SELECT\n" +
            "        cities.id AS city_id,\n" +
            "        cities.name_city AS city_name,\n" +
            "        cities.image_id AS city_image_id,\n" +
            "        COUNT(DISTINCT tickets.user_id) AS people_count\n" +
            "    FROM\n" +
            "        tickets\n" +
            "            JOIN schedules ON tickets.schedule_id = schedules.id\n" +
            "            JOIN routes ON schedules.route_id = routes.id\n" +
            "            JOIN stations ON routes.destination_station_id = stations.id\n" +
            "            JOIN cities ON stations.city_id = cities.id\n" +
            "    WHERE tickets.status = 'Complete'\n" +
            "    GROUP BY cities.id\n" +
            "),\n" +
            "     CityHotelCount AS (\n" +
            "         SELECT\n" +
            "             cities.id AS city_id,\n" +
            "             cities.name_city AS city_name,\n" +
            "             cities.image_id AS city_image_id,\n" +
            "             COUNT(DISTINCT booking_hotels.user_id) AS people_count\n" +
            "         FROM\n" +
            "             booking_hotels\n" +
            "                 JOIN booking_hotels_details ON booking_hotels.id = booking_hotels_details.booking_id\n" +
            "                 JOIN rooms ON booking_hotels_details.room_id = rooms.id\n" +
            "                 JOIN hotels ON rooms.hotel_id = hotels.id\n" +
            "                 JOIN account_enterprises ON hotels.enterprise_id = account_enterprises.id\n" +
            "                 JOIN cities ON account_enterprises.city_id = cities.id\n" +
            "         WHERE booking_hotels.status = 'Complete'\n" +
            "         GROUP BY cities.id\n" +
            "     )\n" +
            "SELECT\n" +
            "    combined.city_id,\n" +
            "    combined.city_name,\n" +
            "    i.url AS city_image_url,\n" +
            "    COALESCE(SUM(combined.people_count), 0) AS total_people_count\n" +
            "FROM (\n" +
            "         SELECT city_id, city_name, city_image_id, people_count FROM CityTourCount\n" +
            "         UNION ALL\n" +
            "         SELECT city_id, city_name, city_image_id, people_count FROM CityHotelCount\n" +
            "     ) AS combined\n" +
            "         JOIN images i ON i.id = combined.city_image_id\n" +
            "GROUP BY combined.city_id, combined.city_name, i.url\n" +
            "ORDER BY total_people_count DESC\n" +
            "LIMIT 3;")
    List<Map<String, Object>> getTop3CitiesPopular();
}
