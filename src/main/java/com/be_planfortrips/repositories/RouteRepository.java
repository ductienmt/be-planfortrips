package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Route;
import com.be_planfortrips.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route,String> {
    boolean existsByOriginStationAndDestinationStationAndIdNot(Station originStation, Station destinationStation, String id);
}
