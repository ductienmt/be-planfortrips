package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.CouponDto;
import com.be_planfortrips.dto.response.CouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ICouponService {
    CouponResponse createCoupon(CouponDto CouponDto) throws Exception;
    CouponResponse updateCoupon(Integer id, CouponDto CouponDto) throws Exception;
    Page<CouponResponse> getCoupons(PageRequest request);
    CouponResponse getByCouponId(Integer id) throws Exception;
    void deleteCouponById(Integer id);
}
