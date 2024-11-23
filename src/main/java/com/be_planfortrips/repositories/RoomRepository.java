package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId")
    Page<Room> findByHotelId(@Param("hotelId") Long hotelId, Pageable pageable);

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


    @Override
    boolean existsById(Long roomId);

}
