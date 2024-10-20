package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, String> {
}
