package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.sql.StatisticalCount;
import com.be_planfortrips.dto.sql.StatisticalCountMonth;
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

    @Query(value = """
    SELECT 
        month_table.month AS date,  -- Thay đổi từ 'month' thành 'date'
        COUNT(p.id) AS count  -- Thay đổi từ 'account_count' thành 'count'
    FROM 
        (SELECT 1 AS month UNION ALL 
         SELECT 2 UNION ALL 
         SELECT 3 UNION ALL 
         SELECT 4 UNION ALL 
         SELECT 5 UNION ALL 
         SELECT 6 UNION ALL 
         SELECT 7 UNION ALL 
         SELECT 8 UNION ALL 
         SELECT 9 UNION ALL 
         SELECT 10 UNION ALL 
         SELECT 11 UNION ALL 
         SELECT 12) AS month_table
    LEFT JOIN 
        plan_details p 
        ON EXTRACT(MONTH FROM p.start_date) = month_table.month 
        AND EXTRACT(YEAR FROM p.start_date) = :year
    GROUP BY 
        month_table.month
    ORDER BY 
        month_table.month;
""", nativeQuery = true)
    List<StatisticalCount> countPlansByYear(@Param("year") int year);



    @Query(value = """
        WITH days AS (
            SELECT generate_series(1, 31) AS day
        ),
        daily_stats AS (
            SELECT
                EXTRACT(DAY FROM p.create_at) AS day,
                COUNT(p.id) AS plan_count
            FROM plans p
            WHERE EXTRACT(YEAR FROM p.create_at) = :year
              AND EXTRACT(MONTH FROM p.create_at) = :month
            GROUP BY EXTRACT(DAY FROM p.create_at)
        )
        SELECT d.day, COALESCE(ds.plan_count, 0) AS plan_count
        FROM days d
        LEFT JOIN daily_stats ds ON d.day = ds.day
        ORDER BY d.day;
        """, nativeQuery = true)
    List<StatisticalCountMonth> StatisticalCountPlanByMonth(
            @Param("year") int year, @Param("month") int month);



}
