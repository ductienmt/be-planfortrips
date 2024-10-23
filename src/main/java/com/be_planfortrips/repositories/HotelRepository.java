package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    @Query("select h from Hotel h " +
            " where (:keyword is null or :keyword = '' or h.name like %:keyword% or h.address like %:keyword%)")
    Page<Hotel> searchHotels(Pageable pageable,@Param("keyword") String keyword);
}
