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


    List<Ticket> findByCreateAtBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT COUNT(t) FROM Ticket t JOIN Schedule s ON t.schedule.id = s.id WHERE s.vehicleCode.code = :vehicleCode")
    long countTicketsByVehicleCode(@Param("vehicleCode") String vehicleCode);

//    @Query(nativeQuery = true, value = "SELECT\n" +
//            "    t.id AS ticket_id,\n" +
//            "    u.fullname AS customer_name,\n" +
//            "    STRING_AGG(seat.seat_number::text, ', ') AS seat_numbers,\n" +
//            "    u.phonenumber AS customer_phone,\n" +
//            "    t.total_price AS total_price,\n" +
//            "    start_station.name AS departure,\n" +
//            "    end_station.name AS destination,\n" +
//            "    cc.phone_number AS car_company_phone\n" +
//            "FROM tickets t\n" +
//            "         JOIN users u ON t.user_id = u.id\n" +
//            "         JOIN schedules s ON t.schedule_id = s.id\n" +
//            "         JOIN routes r ON s.route_id = r.id\n" +
//            "         JOIN vehicles v ON s.vehicle_code = v.code\n" +
//            "         JOIN car_company cc ON v.car_company_id = cc.id\n" +
//            "         JOIN ticket_seats ts ON ts.ticket_id = t.id\n" +
//            "         JOIN seats seat ON ts.seat_id = seat.id\n" +
//            "         JOIN stations start_station ON r.origin_station_id = start_station.id\n" +
//            "         JOIN stations end_station ON r.destination_station_id = end_station.id\n" +
//            "WHERE cc.enterprise_id = :enterpriseId\n" +
//            "and  t.status = :status\n" +
//            "GROUP BY t.id, u.fullname, u.phonenumber, t.total_price, start_station.name, end_station.name, cc.phone_number;")
//    List<Map<String, Object>> getUsers(@Param(("enterpriseId")) Long enterpriseId, @Param("status") Status status);

    @Query(nativeQuery = true, value = """
    SELECT
        t.id AS ticket_id,
        u.fullname AS customer_name,
        STRING_AGG(seat.seat_number::text, ', ') AS seat_numbers,
        u.phonenumber AS customer_phone,
        t.total_price AS total_price,
        start_station.name AS departure,
        end_station.name AS destination,
        cc.phone_number AS car_company_phone,
        t.status AS status
    FROM tickets t
             JOIN users u ON t.user_id = u.id
             JOIN schedules s ON t.schedule_id = s.id
             JOIN routes r ON s.route_id = r.id
             JOIN vehicles v ON s.vehicle_code = v.code
             JOIN car_company cc ON v.car_company_id = cc.id
             JOIN ticket_seats ts ON ts.ticket_id = t.id
             JOIN seats seat ON ts.seat_id = seat.id
             JOIN stations start_station ON r.origin_station_id = start_station.id
             JOIN stations end_station ON r.destination_station_id = end_station.id
    WHERE cc.enterprise_id = :enterpriseId
      AND t.status = :status
    GROUP BY t.id, u.fullname, u.phonenumber, t.total_price, start_station.name, end_station.name, cc.phone_number;
""")
    List<Map<String, Object>> getUsers(@Param("enterpriseId") Long enterpriseId, @Param("status") String status);




}

