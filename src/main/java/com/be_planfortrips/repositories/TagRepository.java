package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    Boolean existsByTagName(String tagName);

    Optional<Tag> findByTagName(String tagName);
}
