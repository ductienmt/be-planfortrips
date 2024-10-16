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
    public ResponseEntity<?> getAllPlanByUserId(@RequestParam("userId") Integer userId) {
        try {
            return ResponseEntity.ok().body(this.planService.getAllPlanByUserId(userId.longValue()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getEssentialData")
    public ResponseEntity<?> planTrip() {
        return null;
    }


}
