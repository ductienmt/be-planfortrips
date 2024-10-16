package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId")
    List<Room> findByHotelId(@Param("hotelId") Long hotelId);

    @Query(value = "SELECT r.* " +
            "FROM rooms r " +
            "WHERE r.is_available = TRUE " +
            "AND r.max_size >= :numberOfPeople " +
            "AND r.id NOT IN ( " +
            "SELECT b.room_id " +
            "FROM booking_hotels_details b " +
            "WHERE b.check_in_time < :checkOutDate " +
            "AND b.check_out_time > :checkInDate)",
            nativeQuery = true)
    List<Room> findAvailableRooms(@Param("checkInDate") LocalDateTime checkInDate,
                                  @Param("checkOutDate") LocalDateTime checkOutDate,
                                  @Param("numberOfPeople") int numberOfPeople);

}
