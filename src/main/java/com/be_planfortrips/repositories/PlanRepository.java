package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long>{
    @Query("SELECT p FROM Plan p WHERE p.user.id = :userId")
    List<Plan> findAllByUserId(@Param("userId") Long userId);

    @Query("Select count(*) from Plan ")
    Integer getCountPlan();

    @Query("SELECT p FROM Plan p WHERE p.user.id = :userId AND " +
            "(:departureDate <= p.endDate AND :returnDate >= p.startDate)")
    List<Plan> findOverlappingPlans(@Param("userId") Long userId,
                                    @Param("departureDate") LocalDate departureDate,
                                    @Param("returnDate") LocalDate returnDate);
}
