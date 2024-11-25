package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Checkin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    @Query("SELECT c FROM Checkin c WHERE c.city.nameCity like concat('%', :cityName, '%')")
    Page<Checkin> findByCityName(@Param("cityName") String cityName, Pageable pageable);

    @Query("SELECT c FROM Checkin c WHERE c.name like concat('%', :name, '%')")
    Page<Checkin> findByName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT c FROM Checkin c where c.city.nameCity like %:cityName% ORDER BY RANDOM() LIMIT :limit")
    List<Checkin> findRandomCheckins(@Param("limit") int limit, @Param("cityName") String cityName);

    @Query("SELECT c FROM Checkin c WHERE c.city.id = :cityId")
    List<Checkin> findByCityId(@Param("cityId") String city);


}
