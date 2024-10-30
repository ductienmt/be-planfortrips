package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.controllers.PlanController;
import com.be_planfortrips.dto.PlanDto;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.dto.response.PlanResponse;
import com.be_planfortrips.dto.response.PlanResponseDetail;

import java.util.List;
import java.util.Map;

public interface IPlanService {
    Map<String, Object> prepareDataPlan(DataEssentialPlan dataEssentialPlan);
    List<PlanResponse> getAllPlan();
    List<PlanResponse> getAllPlanByUserId();
    PlanResponse getPlanById(Long id);
    PlanResponseDetail getPlanDetail(Long id);
    void save(PlanDto planDto);
}
