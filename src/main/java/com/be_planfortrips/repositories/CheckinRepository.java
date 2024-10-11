package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Checkin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    @Query("SELECT c FROM Checkin c WHERE c.city.nameCity like concat('%', :cityName, '%')")
    Page<Checkin> findByCityName(@Param("cityName") String cityName, Pageable pageable);

    @Query("SELECT c FROM Checkin c WHERE c.name like concat('%', :name, '%')")
    Page<Checkin> findByName(@Param("name") String name, Pageable pageable);

}
