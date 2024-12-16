package com.be_planfortrips.controllers;

import com.be_planfortrips.services.impl.AccommodationDashboardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${api.prefix}/accommodation-dashboard")
public class AccommodationDashboard {

    @Autowired
    private AccommodationDashboardServiceImpl accommodationDashboardService;

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue(@RequestParam("time") String time) {
        return ResponseEntity.ok(accommodationDashboardService.getRevenue(time));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {
        return ResponseEntity.ok(accommodationDashboardService.getInfo());
    }

    @GetMapping("/feedback")
    public ResponseEntity<?> getFeedback() {
        return ResponseEntity.ok(accommodationDashboardService.getFeedback());
    }
}
