package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.response.StatisticalCountYear;
import com.be_planfortrips.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long>{
    @Query("SELECT p FROM Plan p WHERE p.user.id = :userId")
    List<Plan> findAllByUserId(@Param("userId") Long userId);

    @Query("Select count(*) from Plan ")
    Integer getCountPlan();

    @Query(value = "SELECT \n" +
            "    month_table.month, \n" +
            "    COUNT(p.id) AS account_count\n" +
            "FROM \n" +
            "    (SELECT 1 AS month UNION ALL \n" +
            "     SELECT 2 UNION ALL \n" +
            "     SELECT 3 UNION ALL \n" +
            "     SELECT 4 UNION ALL \n" +
            "     SELECT 5 UNION ALL \n" +
            "     SELECT 6 UNION ALL \n" +
            "     SELECT 7 UNION ALL \n" +
            "     SELECT 8 UNION ALL \n" +
            "     SELECT 9 UNION ALL \n" +
            "     SELECT 10 UNION ALL \n" +
            "     SELECT 11 UNION ALL \n" +
            "     SELECT 12) AS month_table\n" +
            "LEFT JOIN \n" +
            "    plan_details p \n" +
            "    ON EXTRACT(MONTH FROM p.start_date) = month_table.month \n" +
            "    AND EXTRACT(YEAR FROM p.start_date) = :year\n" +
            "GROUP BY \n" +
            "    month_table.month\n" +
            "ORDER BY \n" +
            "    month_table.month;\n", nativeQuery = true)
    List<StatisticalCountYear> countPlansByYear(@Param("year") int year);


}
