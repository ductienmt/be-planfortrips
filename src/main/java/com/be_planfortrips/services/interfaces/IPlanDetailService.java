package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.entity.PlanDetail;

import java.util.List;

public interface IPlanDetailService {
    List<PlanDetail> getAllPlanDetailByPlanId(Long planId);

}
