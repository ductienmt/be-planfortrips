package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    boolean existsByCode(String code);
    Coupon findByCode(String code);
    @Query("select c from Coupon c " +
            "where (:id is null or c.accountEnterprise.accountEnterpriseId = :id)")
    Page<Coupon> getCouponByEnterpriseId(Pageable pageable, @Param("id") Long id);

    @Query("select c from Coupon c " +
            "where c.accountEnterprise.accountEnterpriseId IS NULL")
    Page<Coupon> getCouponForAdmin(Pageable pageable);

    @Query("select c from Coupon c " +
            "where c.code = :code and c.isActive = :status and c.useCount < c.useLimit")
    Coupon findByCodeAndStatus(@Param("code") String code,@Param("status") Boolean status);

    @Query("select c from Coupon c " +
            "where (:id is null or c.accountEnterprise.accountEnterpriseId = :id) and c.isActive = :status")
    Page<Coupon> getCouponByEnterpriseIdAndStatus(Pageable pageable, @Param("id") Long id, @Param("status") Boolean status);
}

