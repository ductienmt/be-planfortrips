package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.FlightPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightPriceRepository extends JpaRepository<FlightPrice, Long> {
}
