package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long>{
    @Query("SELECT p FROM Plan p WHERE p.user.id = :userId")
    List<Plan> findAllByUserId(@Param("userId") Long userId);
}
