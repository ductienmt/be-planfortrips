package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {
}
