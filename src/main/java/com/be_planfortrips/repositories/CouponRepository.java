package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    boolean existsByCode(String code);
    Coupon findByCode(String code);
}

