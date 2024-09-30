package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
}
