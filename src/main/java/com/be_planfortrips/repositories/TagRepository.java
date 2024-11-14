package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Boolean existsByName(String name);
    List<Tag> findAllByNameIn(List<String> name);
}
