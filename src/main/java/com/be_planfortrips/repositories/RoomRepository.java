package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
