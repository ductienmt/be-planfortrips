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
}
