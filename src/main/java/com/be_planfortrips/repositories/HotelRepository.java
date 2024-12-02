package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.HotelAmenities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    @Query("select h from Hotel h " +
            " where (:keyword is null or :keyword = '' or h.name like %:keyword% or h.address like %:keyword% " +
            "and (:rating is null or h.rating <= :rating))")
    Page<Hotel> searchHotels(Pageable pageable,@Param("keyword") String keyword,@Param("rating") Integer rating);

    @Query("select h from Hotel h where h.accountEnterprise.accountEnterpriseId = :enterpriseId")
    List<Hotel> findByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
    @Query("select h from Hotel h inner join AccountEnterprise ae " +
            "ON ae.accountEnterpriseId = h.accountEnterprise.accountEnterpriseId \n" +
            "inner join City c ON c.id = ae.city.id \n" +
            "inner join Station s ON s.city.id = c.id \n" +
            "INNER JOIN Route r ON r.originStation.id = s.id OR r.destinationStation.id = s.id \n" +
            "where r.id = :route_id")
    List<Hotel> findHotelByRouteId(@Param("route_id") String routeId);

    @Query(value = "SELECT h, r " +
            "FROM Hotel h " +
            "JOIN h.rooms r " +
            "WHERE r.price BETWEEN :minPrice AND :maxPrice")
    List<Object[]> findHotelsWithRoomsInPriceRange(@Param("minPrice") BigDecimal minPrice,
                                                   @Param("maxPrice") BigDecimal maxPrice);}
