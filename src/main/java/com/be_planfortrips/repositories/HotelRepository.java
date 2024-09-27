package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Page<Hotel> findAll(Pageable pageable);
}
