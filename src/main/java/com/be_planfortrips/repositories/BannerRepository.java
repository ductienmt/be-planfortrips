package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long>{
    @Query("SELECT b FROM Banner b WHERE b.isActive = true")
    List<Banner> findAllUser();
}
