package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Route;
import com.be_planfortrips.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


}
