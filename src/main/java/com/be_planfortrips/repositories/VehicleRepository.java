package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.sql.StatisticalResource;
import com.be_planfortrips.entity.TypeVehicle;
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

    @Query(nativeQuery = true, value = "SELECT DISTINCT v.*\n" +
            "FROM vehicles v\n" +
            "         JOIN schedules s ON v.code = s.vehicle_code\n" +
            "         JOIN public.car_company cc ON v.car_company_id = cc.id\n" +
            "WHERE\n" +
            "    (s.departure_time::date = CURRENT_DATE OR\n" +
            "    s.arrival_time::date = CURRENT_DATE)\n" +
            "    AND cc.enterprise_id = :id;")
    List<Vehicle> getTheRunningVehicle(@Param("id") Long enterpriseId);

    @Query(nativeQuery = true, value = "SELECT v.*\n" +
            "FROM vehicles v\n" +
            "         JOIN car_company cc ON v.car_company_id = cc.id\n" +
            "WHERE cc.enterprise_id = :enterpriseId\n" +
            "  AND NOT EXISTS (\n" +
            "    SELECT 1\n" +
            "    FROM schedules s\n" +
            "    WHERE s.vehicle_code = v.code\n" +
            "      AND (\n" +
            "        s.departure_time::date BETWEEN CURRENT_DATE AND CURRENT_DATE + 1\n" +
            "            OR s.arrival_time::date BETWEEN CURRENT_DATE AND CURRENT_DATE + 1\n" +
            "        )\n" +
            ");")
    List<Vehicle> getNotInUseVehicle(@Param("enterpriseId") Long enterpriseId);

    @Query("select v from Vehicle v where v.carCompany.enterprise.accountEnterpriseId = :id " +
            "and (:keyword is null or v.code ilike %:keyword% or v.driverName ilike %:keyword% " +
            "or v.driverPhone ilike %:keyword% or v.plateNumber ilike %:keyword%)")
    List<Vehicle> getVehicleByEntepriseIdAndWithKeyword(@Param("keyword") String keyword, @Param("id") Long id);

    @Query("select v from Vehicle v where v.typeVehicle = :type and v.carCompany.enterprise.accountEnterpriseId = :id")
    List<Vehicle> getVehicleByTypeAndEnterpriseId(@Param("type") TypeVehicle type, @Param("id") Long id);

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

    @Query("select count(v) from Vehicle v where v.carCompany.enterprise.accountEnterpriseId = :enterpriseId")
    Integer countByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

}
