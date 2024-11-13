package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour,Integer> {
    @Query("select t from Tour t " +
            "inner join t.tags h " +
            "where (:titleName is null or :titleName = '' or t.title like %:titleName%) " +
            "and (:rating is null or t.rating <= :rating) " +
            "and (:tags is null or h.name in :tags)")
    Page<Tour> searchTours(Pageable pageable,
                           @Param("titleName") String titleName,
                           @Param("rating") Integer rating,
                           @Param("tags") List<String> tags);
}
