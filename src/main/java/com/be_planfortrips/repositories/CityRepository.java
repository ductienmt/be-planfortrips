package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, String> {
    List<City> findByArea_Id(String area_id);
}
