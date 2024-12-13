package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Plan;
import com.be_planfortrips.entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PlanDetailRepository extends JpaRepository<PlanDetail, Long> {
    @Query("SELECT pd FROM PlanDetail pd WHERE pd.plan.id = :planId")
    List<PlanDetail> findAllByPlanId(@Param("planId") Long planId);



}
