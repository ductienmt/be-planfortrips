package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.CouponDTO;
import com.be_planfortrips.dto.response.CouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ICouponService {
    CouponResponse createCoupon(CouponDTO CouponDto) throws Exception;
    CouponResponse updateCoupon(Integer id,CouponDTO CouponDto) throws Exception;
    Page<CouponResponse> getCoupons(PageRequest request);
    CouponResponse getByCouponId(Integer id) throws Exception;
    void deleteCouponById(Integer id);
}
