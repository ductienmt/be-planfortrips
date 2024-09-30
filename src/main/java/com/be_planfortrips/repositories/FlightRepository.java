package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
