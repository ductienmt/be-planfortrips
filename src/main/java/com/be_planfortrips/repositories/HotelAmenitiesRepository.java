package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.HotelAmenities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelAmenitiesRepository extends JpaRepository<HotelAmenities, Integer> {
  }