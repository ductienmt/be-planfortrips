package com.be_planfortrips.services.impl;

import com.be_planfortrips.controllers.PlanController;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.dto.response.PlanResponse;
import com.be_planfortrips.entity.Plan;
import com.be_planfortrips.repositories.PlanRepository;
import com.be_planfortrips.services.interfaces.IPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl implements IPlanService {
    private static final String CHATGPT_API = "sk-OI3g1CMDw2XatjAOR2ITR57Siu0oOXP7Dq5vwLzmVlT3BlbkFJyCZHBKMikCuFGb6f_DfnvlrA3PoeKzz000dJWZ0sEA";

    @Autowired
    private PlanRepository planRepository;

    @Override
    public PlanResponse genaratePlan(DataEssentialPlan dataEssentialPlan) {

        return null;
    }

    @Override
    public List<PlanResponse> getAllPlan() {
        return List.of();
    }

    @Override
    public List<PlanResponse> getAllPlanByUserId(Long id) {
        List<Plan> plans = planRepository.findAllByUserId(id);
        return plans.stream().map(
                plan -> {
                    PlanResponse planResponse = new PlanResponse();
                    planResponse.setPlan_id(plan.getId());
                    planResponse.setPlan_name(plan.getPlanName());
                    planResponse.setBudget(plan.getBudget());
                    planResponse.setNumberPeople(plan.getNumberPeople());
                    return planResponse;
                }).collect(Collectors.toList());
    }

    @Override
    public PlanResponse getPlanById(Long id) {

        return null;
    }
}
