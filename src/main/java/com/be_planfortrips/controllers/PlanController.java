package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.FlightDto;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.entity.Flight;
import com.be_planfortrips.repositories.FlightRepository;
import com.be_planfortrips.services.impl.FlightServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/plans")
public class PlanController {

    @Autowired
    private FlightServiceImpl flightService;


//    @PostMapping("/genarate")
//    public ResponseEntity<?> genaratePlan(@RequestBody DataEssentialPlan dataEssentialPlan) {
//        try {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        try {
            List<FlightDto> flights = flightService.getFlightsByCitiesAndDepartureTime(
                    Timestamp.valueOf("2024-10-06 07:00:00"),
                    "Vũng Tàu",
                    "Hồ Chí Minh"
            );
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error");
        }
    }

}
