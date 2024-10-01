package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
