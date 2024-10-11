package com.be_planfortrips.controllers;

import com.be_planfortrips.services.interfaces.IPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/plans")
public class PlanController {
    @Autowired
    private IPlanService planService;

    @GetMapping
    public ResponseEntity<?> getAllPlan() {
        try {
            return ResponseEntity.ok().body(this.planService.getAllPlanByUserId(29L));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
