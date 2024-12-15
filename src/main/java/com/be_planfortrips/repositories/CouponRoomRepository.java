package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.CouponRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CouponRoomRepository extends JpaRepository<CouponRoom, Long> {
    @Query("SELECT r.roomName FROM Room r JOIN CouponRoom cr ON r.id = cr.room.id WHERE cr.coupon.id = :couponId")
    List<String> findRoomCodesByCouponId(@Param("couponId") Long couponId);
}
