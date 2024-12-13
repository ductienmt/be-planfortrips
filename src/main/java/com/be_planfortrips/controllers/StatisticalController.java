package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.response.StatisticalCountYear;
import com.be_planfortrips.dto.response.StatisticalResource;
import com.be_planfortrips.dto.sql.StatisticalBookingHotelDetail;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.services.StatisticalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/statistical")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class StatisticalController {

    UserRepository userRepository;
    AdminRepository adminRepository;
    AccountEnterpriseRepository accountEnterpriseRepository;
    PlanRepository planRepository;
    PlanDetailRepository planDetailRepository;

    StatisticalService statisticalService;
    VehicleRepository vehicleRepository;

    @GetMapping("/user")
    public ResponseEntity<Integer> getCountUser() {
        return ResponseEntity.ok(userRepository.countAll());
    }

    @GetMapping("/user/{year}")
    public ResponseEntity<List<StatisticalCountYear>> StatisticalUserByYear(
            @PathVariable("year") Integer year
    ) {
        List<StatisticalCountYear> res = userRepository.countUsersByYear(year);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/admin")
    public ResponseEntity<Integer> getCountAdmin() {
        return ResponseEntity.ok(adminRepository.countAll());
    }

    @GetMapping("/enterprise")
    public ResponseEntity<Integer> getCountEnterprise() {
        return ResponseEntity.ok(accountEnterpriseRepository.countAll());
    }

    @GetMapping("/enterprise/{year}")
    public ResponseEntity<List<StatisticalCountYear>> StatisticalEtpByYear(
            @PathVariable Integer year
    ) {
        List<StatisticalCountYear> statisticalCountEtp = accountEnterpriseRepository.StatisticalCountEtpByYear(year);
        return ResponseEntity.ok(statisticalCountEtp);
    }

    @GetMapping("/plan")
    public ResponseEntity<Integer> getCountPlan() {
        return ResponseEntity.ok(planRepository.getCountPlan());
    }

    @GetMapping("/plan/{year}")
    public ResponseEntity<List<StatisticalCountYear>> StatisticalPlanByYear(
            @PathVariable("year") Integer year
    ) {
        List<StatisticalCountYear> res = planRepository.countPlansByYear(year);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/year/bookingHotelDetail/{year}")
    public ResponseEntity<List<StatisticalBookingHotelDetail>>
            statisticalBkdetailByYear(@PathVariable Integer year) {
        List<StatisticalBookingHotelDetail> res = statisticalService.statisticalBookingHotelByYear(year);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/vehicle/{year}")
    public ResponseEntity<List<StatisticalResource>> StatisticalVehicleByYear(
            @PathVariable("year") Integer year
    ) {
        List<StatisticalResource> res = vehicleRepository.getTop1VehicleByYear(year);
        return ResponseEntity.ok(res);
    }
}
