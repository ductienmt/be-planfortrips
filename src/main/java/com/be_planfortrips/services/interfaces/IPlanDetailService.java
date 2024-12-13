package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.response.StatisticalCountYear;
import com.be_planfortrips.entity.PlanDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPlanDetailService {
    List<PlanDetail> getAllPlanDetailByPlanId(Long planId);

}
