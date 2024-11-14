package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.TypeEnterpriseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TypeEnterpriseDetailRepository extends JpaRepository<TypeEnterpriseDetail, Long> {
}
