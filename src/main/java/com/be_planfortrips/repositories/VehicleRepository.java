package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.sql.StatisticalResource;
import com.be_planfortrips.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle,String> {
    Vehicle findByCode(String code);
    @Query("select h from Vehicle h " +
            " where (:keyword is null or :keyword = '' or h.carCompany.name like %:keyword%)")
    Page<Vehicle> searchVehicles(Pageable pageable, @Param("keyword") String keyword);

    @Query("select v from Vehicle v where v.carCompany.enterprise.accountEnterpriseId = :id")
    List<Vehicle> getVehicleByEnterpriseId(@Param("id") Long id);

    @Query(value = "WITH all_months AS (\n" +
            "    SELECT generate_series(1, 12) AS month\n" +
            "),\n" +
            "     vehicle_schedule AS (\n" +
            "         SELECT v.code AS resource_id,\n" +
            "                EXTRACT(MONTH FROM s.create_at) AS month,\n" +
            "                COUNT(s.id) AS count\n" +
            "         FROM vehicles v\n" +
            "                  LEFT JOIN schedules s ON v.code = s.vehicle_code\n" +
            "             AND EXTRACT(YEAR FROM s.create_at) = :year\n" +
            "         GROUP BY v.code, EXTRACT(MONTH FROM s.create_at)\n" +
            "     ),\n" +
            "     ranked_vehicles AS (\n" +
            "         SELECT\n" +
            "             vs.month,\n" +
            "             vs.resource_id,\n" +
            "             vs.count,\n" +
            "             RANK() OVER (PARTITION BY vs.month ORDER BY vs.count DESC) AS rank\n" +
            "         FROM vehicle_schedule vs\n" +
            "     )\n" +
            "SELECT\n" +
            "    am.month AS month,\n" +
            "    rv.resource_id AS resource_id,\n" +
            "    COALESCE(rv.count, 0) AS count\n" +
            "FROM all_months am\n" +
            "         LEFT JOIN ranked_vehicles rv\n" +
            "                   ON am.month = rv.month AND rv.rank = 1\n" +
            "ORDER BY am.month;", nativeQuery = true)
    List<StatisticalResource> getTop1VehicleByYear(@Param("year") int year);

}
