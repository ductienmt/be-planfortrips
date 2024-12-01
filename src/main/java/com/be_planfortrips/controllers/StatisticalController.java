package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.sql.StatisticalBookingHotelDetail;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.AdminRepository;
import com.be_planfortrips.repositories.PlanRepository;
import com.be_planfortrips.repositories.UserRepository;
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

    StatisticalService statisticalService;

    @GetMapping("/user")
    public ResponseEntity<Integer> getCountUser() {
        return ResponseEntity.ok(userRepository.countAll());
    }

    @GetMapping("/admin")
    public ResponseEntity<Integer> getCountAdmin() {
        return ResponseEntity.ok(adminRepository.countAll());
    }

    @GetMapping("/enterprise")
    public ResponseEntity<Integer> getCountEnterprise() {
        return ResponseEntity.ok(accountEnterpriseRepository.countAll());
    }

    @GetMapping("/plan")
    public ResponseEntity<Integer> getCountPlan() {
        return ResponseEntity.ok(planRepository.getCountPlan());
    }

    @GetMapping("/year/bookingHotelDetail/{year}")
    public ResponseEntity<List<StatisticalBookingHotelDetail>>
            statisticalBkdetailByYear(@PathVariable Integer year) {
        List<StatisticalBookingHotelDetail> res = statisticalService.statisticalBookingHotelByYear(year);
        return ResponseEntity.ok(res);
    }
}
