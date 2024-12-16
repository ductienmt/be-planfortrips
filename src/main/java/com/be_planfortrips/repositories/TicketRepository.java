package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Schedule;
import com.be_planfortrips.entity.Status;
import com.be_planfortrips.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    @Query("select t from Ticket t where t.status = 'Pending' and t.createAt < :timeLimit")
    List<Ticket> findPendingTicketsBefore(LocalDateTime timeLimit);
    @Query("select t from Ticket t where t.status = 'Cancelled'")
    List<Ticket> findByStatusCancelled();
    List<Ticket> findByUser_Id(Long id);
    List<Ticket> findBySchedule_Id(Integer id);
    List<Ticket> findByUser_Id(Integer id);


    @Query(nativeQuery = true, value = "SELECT\n" +
//            "    DATE_TRUNC('week', t.create_at) AS week_start,\n" +
            "    TO_CHAR(DATE_TRUNC('week', t.create_at), 'DD-MM-YYYY') AS week_start,\n" +
            "    SUM(t.total_price) AS total_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(DOW FROM t.create_at) = 1) AS monday_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(DOW FROM t.create_at) = 2) AS tuesday_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(DOW FROM t.create_at) = 3) AS wednesday_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(DOW FROM t.create_at) = 4) AS thursday_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(DOW FROM t.create_at) = 5) AS friday_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(DOW FROM t.create_at) = 6) AS saturday_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(DOW FROM t.create_at) = 0) AS sunday_revenue\n" +
            "FROM\n" +
            "    tickets t\n" +
            "        JOIN schedules s ON t.schedule_id = s.id\n" +
            "        JOIN vehicles v ON s.vehicle_code = v.code\n" +
            "        JOIN car_company cc ON v.car_company_id = cc.id\n" +
            "WHERE\n" +
            "    cc.enterprise_id = :enterpriseId\n" +
            "  AND t.status = 'Complete'\n" +
            "GROUP BY\n" +
            "    DATE_TRUNC('week', t.create_at);")
    List<Map<String, Object>> compileRevenueWithWeek(@Param("enterpriseId") Long enterpriseId);

    @Query(nativeQuery = true, value = "SELECT\n" +
//            "    DATE_TRUNC('month', t.create_at) AS month_start,\n" +
            "    TO_CHAR(DATE_TRUNC('month', t.create_at), 'DD-MM-YYYY') AS month_start,\n" +
            "    SUM(t.total_price) AS total_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE DATE_PART('week', t.create_at) = DATE_PART('week', DATE_TRUNC('month', t.create_at))) AS week_1_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE DATE_PART('week', t.create_at) = DATE_PART('week', DATE_TRUNC('month', t.create_at)) + 1) AS week_2_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE DATE_PART('week', t.create_at) = DATE_PART('week', DATE_TRUNC('month', t.create_at)) + 2) AS week_3_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE DATE_PART('week', t.create_at) = DATE_PART('week', DATE_TRUNC('month', t.create_at)) + 3) AS week_4_revenue\n" +
            "FROM\n" +
            "    tickets t\n" +
            "        JOIN schedules s ON t.schedule_id = s.id\n" +
            "        JOIN vehicles v ON s.vehicle_code = v.code\n" +
            "        JOIN car_company cc ON v.car_company_id = cc.id\n" +
            "WHERE\n" +
            "    cc.enterprise_id = :enterpriseId\n" +
            "  AND t.status = 'Complete'\n" +
            "GROUP BY\n" +
            "    DATE_TRUNC('month', t.create_at);")
    List<Map<String, Object>> compileRevenueWithMonth(@Param("enterpriseId") Long enterpriseId);

    @Query(nativeQuery = true, value = "SELECT\n" +
//            "    DATE_TRUNC('year', t.create_at) AS year_start,\n" +
            "    TO_CHAR(DATE_TRUNC('year', t.create_at), 'DD-MM-YYYY') AS year_start,\n" +
            "    SUM(t.total_price) AS total_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 1) AS jan_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 2) AS feb_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 3) AS mar_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 4) AS apr_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 5) AS may_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 6) AS jun_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 7) AS jul_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 8) AS aug_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 9) AS sep_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 10) AS oct_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 11) AS nov_revenue,\n" +
            "    SUM(t.total_price) FILTER (WHERE EXTRACT(MONTH FROM t.create_at) = 12) AS dec_revenue\n" +
            "FROM\n" +
            "    tickets t\n" +
            "        JOIN schedules s ON t.schedule_id = s.id\n" +
            "        JOIN vehicles v ON s.vehicle_code = v.code\n" +
            "        JOIN car_company cc ON v.car_company_id = cc.id\n" +
            "WHERE\n" +
            "    cc.enterprise_id = :enterpriseId\n" +
            "  AND t.status = 'Complete'\n" +
            "GROUP BY\n" +
            "    DATE_TRUNC('year', t.create_at);")
    List<Map<String, Object>> compileRevenueWithYear(@Param("enterpriseId") Long enterpriseId);

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    COUNT(*) AS total_tickets_booked\n" +
            "FROM\n" +
            "    tickets t\n" +
            "        JOIN schedules s ON t.schedule_id = s.id\n" +
            "        JOIN vehicles v ON s.vehicle_code = v.code\n" +
            "        JOIN car_company cc ON v.car_company_id = cc.id\n" +
            "WHERE\n" +
            "    t.status = 'Complete'\n" +
            "  AND cc.enterprise_id = :enterpriseId;")
    Integer countTicketBooked(@Param("enterpriseId") Long enterpriseId);

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    COUNT(*) AS total_tickets_future\n" +
            "FROM\n" +
            "    tickets t\n" +
            "        JOIN schedules s ON t.schedule_id = s.id\n" +
            "        JOIN vehicles v ON s.vehicle_code = v.code\n" +
            "        JOIN car_company cc ON v.car_company_id = cc.id\n" +
            "WHERE\n" +
            "    t.status = 'Pending'\n" +
            "  AND s.departure_time > NOW()\n" +
            "  AND cc.enterprise_id = :enterpriseId;")
    Integer countTicketBookAdvance(@Param("enterpriseId") Long enterpriseId);

}

