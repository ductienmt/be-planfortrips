package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image,Long> {
    @Query("SELECT i FROM Image i WHERE i.url = :name")
    Image findImageByName(@Param("name") String name);
}
