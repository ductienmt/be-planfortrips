package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.controllers.PlanController;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.dto.response.PlanResponse;

import java.util.List;

public interface IPlanService {
    PlanResponse genaratePlan(DataEssentialPlan dataEssentialPlan);
    List<PlanResponse> getAllPlan();
    List<PlanResponse> getAllPlanByUserId(Long id);
    PlanResponse getPlanById(Long id);
}
