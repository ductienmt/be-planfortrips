package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.PlanDto;
import com.be_planfortrips.dto.request.DataEssentialPlan;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.services.interfaces.IPlanService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/plans")
@Slf4j
public class PlanController {
    @Autowired
    private IPlanService planService;

    // Phương thức hỗ trợ để tạo ApiResponse
    private ResponseEntity<ApiResponse<Void>> buildApiResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                ApiResponse.<Void>builder()
                        .code(status.value())
                        .message(message)
                        .build()
        );
    }


    @PostMapping("/save")
    public ResponseEntity<?> savePlan(@Valid @RequestBody PlanDto planDto) {
        try {
            Map<String, Object> res = this.planService.save(planDto);
            return ResponseEntity.ok().body(res);
        } catch (Exception e) {
            log.error("Error: ", e.getMessage());
            return ResponseEntity.badRequest().body("Lưu kế hoạch thất bại");
        }
    }

    @GetMapping("check-time")
    public ResponseEntity<?> checkTime(@RequestParam("departure") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate departureDate,
                                       @RequestParam("return") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate returnDate) {
        try {
            this.planService.checkTime(departureDate, returnDate);
            return buildApiResponse(HttpStatus.OK, "Thời gian hợp lệ");
        } catch (DateTimeParseException e) {
            return buildApiResponse(HttpStatus.BAD_REQUEST, ErrorType.notValidDateFormat.getMessage());
        } catch (AppException e) {
            return buildApiResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (JsonParseException e) {
            return buildApiResponse(HttpStatus.BAD_REQUEST, ErrorType.notValidDateFormat.getMessage());
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
    public ResponseEntity<?> prepareDataPlan(@Valid @RequestBody DataEssentialPlan dataEssentialPlan) {
        Map<String, Object> response = this.planService.prepareDataPlan(dataEssentialPlan);
        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .code(HttpStatus.OK.value())
                .data(response)
                .message("")
                .build());

    }


}
