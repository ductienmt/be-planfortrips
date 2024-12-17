package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Route;
import com.be_planfortrips.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface RouteRepository extends JpaRepository<Route,String> {
    boolean existsByOriginStationAndDestinationStationAndIdNot(Station originStation, Station destinationStation, String id);
    @Query("SELECT r FROM Route r " +
            "INNER JOIN Station so ON r.originStation.id = so.id " +
            "INNER JOIN Station sd ON r.destinationStation.id = sd.id " +
            "WHERE ((:keywordSo IS NULL OR :keywordSo = '' OR so.name LIKE %:keywordSo%) " +
            "AND (:keywordSd IS NULL OR :keywordSd = '' OR sd.name LIKE %:keywordSd%))")
    Page<Route> getRouteByOriginStation_NameAndDestinationStation_Name(Pageable pageable,
                                                                       @Param("keywordSo") String keywordSo,
                                                                       @Param("keywordSd") String keywordSd);

    @Query(nativeQuery = true, value = "WITH relevance_routes AS (\n" +
            "    SELECT\n" +
            "        r.id AS route_id,\n" +
            "        s1.city_id AS origin_city_id,\n" +
            "        s2.city_id AS destination_city_id,\n" +
            "        ae.city_id AS enterprise_city_id,\n" +
            "        CASE\n" +
            "            WHEN s1.city_id = ae.city_id THEN 1\n" +
            "            WHEN s2.city_id = ae.city_id AND s1.city_id != ae.city_id THEN 3\n" +
            "            ELSE 4\n" +
            "            END AS relevance\n" +
            "    FROM routes r\n" +
            "             LEFT JOIN stations s1 ON r.origin_station_id = s1.id\n" +
            "             LEFT JOIN stations s2 ON r.destination_station_id = s2.id\n" +
            "             JOIN account_enterprises ae ON ae.id = :enterprise_id\n" +
            "),\n" +
            "     reversed_routes AS (\n" +
            "         SELECT\n" +
            "             rr.route_id AS route_id,\n" +
            "             rr.destination_city_id AS origin_city_id,\n" +
            "             rr.origin_city_id AS destination_city_id,\n" +
            "             2 AS relevance\n" +
            "         FROM relevance_routes rr\n" +
            "         WHERE rr.relevance = 1\n" +
            "     )\n" +
            "SELECT *\n" +
            "FROM (\n" +
            "         SELECT route_id, origin_city_id, destination_city_id, relevance FROM relevance_routes\n" +
            "         UNION ALL\n" +
            "         SELECT route_id, origin_city_id, destination_city_id, relevance FROM reversed_routes\n" +
            "     ) combined_routes\n" +
            "ORDER BY\n" +
            "    relevance DESC,\n" +
            "    route_id DESC;")
    List<Map<String, Object>> getRouteRelevance(@Param("enterprise_id") Long enterpriseId);


}
