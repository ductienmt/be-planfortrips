package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station,Integer> {
}
