package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.response.rsTourResponse.TourScheduleBringData;
import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour,Integer> {
    @Query("select t from Tour t " +
            "inner join t.tags h " +
            "where (:titleName is null or :titleName = '' or t.title like %:titleName%) " +
            "and (:rating is null or t.rating <= :rating) " +
            "and (:tags is null or h.name in :tags) " +
            "and t.isActive = true")
    Page<Tour> searchTours(Pageable pageable,
                           @Param("titleName") String titleName,
                           @Param("rating") Integer rating,
                           @Param("tags") List<String> tags);
    @Query("select t from Tour t where t.isActive = true")
    Page<Tour> findAllByActive(PageRequest request);

    @Query(value = "SELECT car_company.id as carCompanyId,\n" +
            "       vehicles.code as vehicleCode,\n" +
            "       routes.id as routeId,\n" +
            "       schedules.id as scheduleId,\n" +
            "       stations.id as stationId\n" +
            "FROM car_company\n" +
            "         JOIN vehicles ON car_company.id = vehicles.car_company_id\n" +
            "         JOIN schedules ON schedules.vehicle_code = vehicles.code\n" +
            "         JOIN routes ON schedules.route_id = routes.id\n" +
            "         JOIN stations ON routes.destination_station_id = stations.id\n" +
            "WHERE schedules.departure_time >= :min\n" +
            "  AND schedules.departure_time < :max\n" +
            "  AND stations.city_id = :cityId;" +
            "  ", nativeQuery = true)
    List<TourScheduleBringData> getSchedulesByDate(@Param("min") LocalDateTime min,
                                                   @Param("max") LocalDateTime max,
                                                   @Param("cityId") String cityId);



}
