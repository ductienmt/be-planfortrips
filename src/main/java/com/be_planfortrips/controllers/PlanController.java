package com.be_planfortrips.controllers;

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

    @GetMapping
    public ResponseEntity<?> getAllPlanByUserId(@RequestParam("userId") Integer userId) {
        try {
            return ResponseEntity.ok().body(this.planService.getAllPlanByUserId(userId.longValue()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/prepare")
    public ResponseEntity<?> prepareDataPlan(@RequestBody DataEssentialPlan dataEssentialPlan) {

            // Gọi service xử lý dữ liệu kế hoạch
            Map<String, Object> response = this.planService.prepareDataPlan(dataEssentialPlan);

            // Trả về kết quả thành công
            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .code(HttpStatus.OK.value())
                    .data(response)
                    .message("")
                    .build());

    }



}
