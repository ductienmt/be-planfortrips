package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
