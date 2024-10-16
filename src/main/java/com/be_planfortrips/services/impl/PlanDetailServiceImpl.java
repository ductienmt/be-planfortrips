package com.be_planfortrips.services.impl;

import com.be_planfortrips.entity.PlanDetail;
import com.be_planfortrips.repositories.PlanDetailRepository;
import com.be_planfortrips.services.interfaces.IPlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanDetailServiceImpl implements IPlanDetailService {
    @Autowired
    private PlanDetailRepository planDetailRepository;


    @Override
    public List<PlanDetail> getAllPlanDetailByPlanId(Long planId) {
        return this.planDetailRepository.findAllByPlanId(planId);
    }
}
