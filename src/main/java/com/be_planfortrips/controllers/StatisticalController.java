package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.sql.StatisticalCount;
import com.be_planfortrips.dto.sql.StatisticalCountMonth;
import com.be_planfortrips.dto.sql.StatisticalResource;
import com.be_planfortrips.dto.sql.StatisticalBookingHotelDetail;
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
    private final HotelRepository hotelRepository;

    @GetMapping("/user")
    public ResponseEntity<Integer> getCountUser() {
        return ResponseEntity.ok(userRepository.countAll());
    }

    @GetMapping("/user/{year}")
    public ResponseEntity<List<StatisticalCount>> StatisticalUserByYear(
            @PathVariable("year") Integer year
    ) {
        List<StatisticalCount> res = userRepository.countUsersByYear(year);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/user/{year}/{month}")
    public ResponseEntity<List<StatisticalCount>> StatisticalUserByMonth(
            @PathVariable("year") Integer year, @PathVariable("month") Integer month
    ){
        List<StatisticalCount> res = userRepository.countUsersByMonth(year, month);
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
    public ResponseEntity<List<StatisticalCount>> StatisticalEtpByYear(
            @PathVariable Integer year
    ) {
        List<StatisticalCount> statisticalCountEtp = accountEnterpriseRepository.StatisticalCountEtpByYear(year);
        return ResponseEntity.ok(statisticalCountEtp);
    }


    @GetMapping("/plan")
    public ResponseEntity<Integer> getCountPlan() {
        return ResponseEntity.ok(planRepository.getCountPlan());
    }

    @GetMapping("/plan/{year}")
    public ResponseEntity<List<StatisticalCount>> StatisticalPlanByYear(
            @PathVariable("year") Integer year
    ) {
        List<StatisticalCount> res = planRepository.countPlansByYear(year);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/plan/{year}/{month}")
    public ResponseEntity<List<StatisticalCount>> StatisticalPlanByMonth(
            @PathVariable("year") Integer year,
            @PathVariable("month") Integer month
    ) {
        List<StatisticalCount> res = planRepository.StatisticalCountPlanByMonth(year, month);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/enterprise/{year}/{month}")
    public ResponseEntity<List<StatisticalCount>> StatisticalEtpByMonth(
            @PathVariable("year") Integer year, @PathVariable("month") Integer month
    ) {
        List<StatisticalCount> statisticalCountMonths = accountEnterpriseRepository.StatisticalCountEtpByMonth(year, month);
        return ResponseEntity.ok(statisticalCountMonths);
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

    @GetMapping("/hotel/{year}")
    public ResponseEntity<List<StatisticalResource>> StatisticalHotelByYear(
            @PathVariable("year") Integer year
    ) {
        List<StatisticalResource> res = hotelRepository.getTop1HotelByYear(year);
        return ResponseEntity.ok(res);
    }


}
