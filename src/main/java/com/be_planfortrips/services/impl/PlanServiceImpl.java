package com.be_planfortrips.services.impl;

import com.be_planfortrips.controllers.PlanController;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.services.interfaces.IPlanService;
import org.springframework.stereotype.Service;

@Service
public class PlanServiceImpl implements IPlanService {
    private static final String CHATGPT_API = "sk-OI3g1CMDw2XatjAOR2ITR57Siu0oOXP7Dq5vwLzmVlT3BlbkFJyCZHBKMikCuFGb6f_DfnvlrA3PoeKzz000dJWZ0sEA";

    @Override
    public PlanController genaratePlan(DataEssentialPlan dataEssentialPlan) {

        return null;
    }
}
