package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.controllers.PlanController;
import com.be_planfortrips.dto.request.DataEssentialPlan;

public interface IPlanService {
    PlanController genaratePlan(DataEssentialPlan dataEssentialPlan);
}
