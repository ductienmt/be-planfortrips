package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Room;
import com.be_planfortrips.entity.TypeOfRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId " +
            "AND r.id NOT IN (" +
            "  SELECT b.room.id FROM BookingHotelDetail b " +
            "  WHERE cast(b.checkInTime as date) < :checkOutDate AND cast(b.checkOutTime as date) > :checkInDate" +
            ")")
    Page<Room> findByHotelId(@Param("hotelId") Long hotelId,
                             @Param("checkInDate") LocalDate checkInDate,
                             @Param("checkOutDate") LocalDate checkOutDate, Pageable pageable);

    @Query("SELECT r FROM Room r " +
            "WHERE r.isAvailable = true " +
            "AND (:destination IS NULL OR r.hotel.accountEnterprise.city.nameCity LIKE %:destination%) " +
            "AND r.id NOT IN (" +
            "  SELECT b.room.id FROM BookingHotelDetail b " +
            "  WHERE b.checkInTime < :checkOutDate AND b.checkOutTime > :checkInDate" +
            ")")
    List<Room> findAvailableRooms(@Param("checkInDate") LocalDateTime checkInDate,
                                  @Param("checkOutDate") LocalDateTime checkOutDate,
                                  @Param("destination") String destination);


    @Query("SELECT r FROM Room r " +
            "LEFT JOIN BookingHotelDetail b ON b.room.id = r.id " +
            "WHERE r.isAvailable = true " +
            "AND (:destination IS NULL OR r.hotel.accountEnterprise.city.nameCity LIKE %:destination%) " +
            "AND (b.room.id IS NULL OR (b.checkOutTime <= :checkInDate OR b.checkInTime >= :checkOutDate))")
    List<Room> findAvailableRoomsNew(@Param("checkInDate") LocalDateTime checkInDate,
                                     @Param("checkOutDate") LocalDateTime checkOutDate,
                                     @Param("destination") String destination);

    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND r.id NOT IN " +
            "(SELECT bhd.room.id FROM BookingHotelDetail bhd WHERE " +
            "(:currentDate BETWEEN bhd.checkInTime AND bhd.checkOutTime))" +
            "AND r.isAvailable = true")
    Page<Room> findAvailableRoomsByHotelIdAndDate(@Param("hotelId") Long hotelId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);

    @Query("SELECT bhd.room FROM BookingHotelDetail bhd WHERE bhd.room.hotel.id = :hotelId " +
            "AND (:currentDate BETWEEN bhd.checkInTime AND bhd.checkOutTime)")
    Page<Room> findBookedRoomsByHotelIdAndDate(@Param("hotelId") Long hotelId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE " +
            "(:hotelId IS NULL OR r.hotel.id = :hotelId) AND " +
            "(:status IS NULL OR r.isAvailable = :status) AND " +
            "(:roomType IS NULL OR r.typeOfRoom = :roomType)")
    Page<Room> filterRoom(
            @Param("hotelId") Long hotelId,
            @Param("status") Boolean status,
            @Param("roomType") TypeOfRoom roomType,
            Pageable pageable
    );

    @Query("SELECT r FROM Room r WHERE r.hotel.accountEnterprise.accountEnterpriseId = :enterpriseId")
    List<Room> findByEnterpriseId(@Param("enterpriseId") Long etpId);


    @Override
    boolean existsById(Long roomId);

}
