package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.PlanDto;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.services.interfaces.IPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/plans")
@Slf4j
public class PlanController {
    @Autowired
    private IPlanService planService;

    @PostMapping("/save")
    public ResponseEntity<?> savePlan(@RequestBody PlanDto planDto) {
        try {
            this.planService.save(planDto);
            return ResponseEntity.ok().body("Lưu kế hoạch thành công");
        } catch (Exception e) {
            log.error("Error: ", e.getMessage());
            return ResponseEntity.badRequest().body("Lưu kế hoạch thất bại");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPlanByUserId() {
        try {
            return ResponseEntity.ok().body(this.planService.getAllPlanByUserId());
        } catch (Exception e) {
            log.error("Error: ", e.getMessage());
            return ResponseEntity.badRequest().body("Lấy danh sách kế hoạch thất bại");
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getPlanById(@RequestParam("id") Integer id) {
        try {
            return ResponseEntity.ok().body(this.planService.getPlanDetail(id.longValue()));
        } catch (Exception e) {
            log.error("Error: ", e.getMessage());
            return ResponseEntity.badRequest().body("Lấy thông tin kế hoạch thất bại");
        }
    }

    @PostMapping("/prepare")
    public ResponseEntity<?> prepareDataPlan(@RequestBody DataEssentialPlan dataEssentialPlan) {
        Map<String, Object> response = this.planService.prepareDataPlan(dataEssentialPlan);
        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .code(HttpStatus.OK.value())
                .data(response)
                .message("")
                .build());

    }


}
