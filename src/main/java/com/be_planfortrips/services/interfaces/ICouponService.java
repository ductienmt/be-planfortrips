package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.CouponDto;
import com.be_planfortrips.dto.response.CouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface ICouponService {
    CouponResponse createCoupon(CouponDto CouponDto) throws Exception;
    CouponResponse updateCoupon(Integer id, CouponDto CouponDto) throws Exception;
    Page<CouponResponse> getCoupons(PageRequest request, Long id);
    CouponResponse getByCouponId(Integer id) throws Exception;
    void deleteCouponById(Integer id);
    CouponResponse getByCouponCode(String code, String status);
    Page<CouponResponse> getByEnterpriseId(Pageable pageable, String status);
}

