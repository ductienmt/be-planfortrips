package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Coupon;
import com.be_planfortrips.entity.DiscountType;
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
    Coupon findByCodeAndStatus(@Param("code") String code, @Param("status") Boolean status);

    @Query("select c from Coupon c " +
            "where (:id is null or c.accountEnterprise.accountEnterpriseId = :id) " +
            "and (:status is null or c.isActive = :status)")
    Page<Coupon> getCouponByEnterpriseIdAndStatus(Pageable pageable, @Param("id") Long id, @Param("status") Boolean status);

    @Query("select c from Coupon c " +
            "where (:keyword is null or c.code ilike %:keyword%) " +
            "and (:status is null or c.isActive = :status) " +
            "and (:discountType is null or c.discountType = :discountType) " +
            "and  c.accountEnterprise.accountEnterpriseId = :enterpriseId")
    Page<Coupon> searchEnterprise(@Param("keyword") String keyword, @Param("status") Boolean status,
                                  @Param("discountType") DiscountType discountType, @Param("enterpriseId") Long id,
                                  Pageable pageable);
}