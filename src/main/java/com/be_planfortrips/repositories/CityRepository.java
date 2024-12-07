package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City, String> {
    List<City> findByArea_Id(String area_id);

    @Query("SELECT c.id, c.nameCity, c.description FROM City c")
    List<Object[]> getAllCities();
    List<City> searchByNameCityContaining(String nameCity);
}
