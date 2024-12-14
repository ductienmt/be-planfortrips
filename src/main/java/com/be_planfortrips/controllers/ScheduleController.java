package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.request.DataSchedule;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.ScheduleResponse;
import com.be_planfortrips.entity.Schedule;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.services.interfaces.IScheduleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/schedules")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleController {

    IScheduleService scheduleService;


    @GetMapping("/all")
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
        List<ScheduleResponse> responses = scheduleService.getAllSchedule();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("getById/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getScheduleById(
            @PathVariable Integer scheduleId) {
        ScheduleResponse response = scheduleService.getScheduleById(scheduleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/vehicle/{vehicleCode}/route/{routeId}")
    public ResponseEntity<List<ScheduleResponse>> getScheduleByRouteId(
            @PathVariable String vehicleCode, @PathVariable String routeId
    ) {
        List<ScheduleResponse> responses =
                scheduleService.getScheduleByVehicleCodeAndRouteId(vehicleCode, routeId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/create")
    public ResponseEntity<ScheduleResponse> createSchedule(
            @RequestBody ScheduleDto scheduleDto) {
        ScheduleResponse response = scheduleService.createSchedule(scheduleDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("update/{scheduleId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable Integer scheduleId, @RequestBody @Valid ScheduleDto scheduleDto) {
        ScheduleResponse response = scheduleService.updateSchedule(scheduleDto, scheduleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("delete/{scheduleId}")
    public ResponseEntity<Void> deleteScheduleById(
            @PathVariable Integer scheduleId) {
        scheduleService.deleteScheduleById(scheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getStationByScheduleId")
    public ResponseEntity<?> getStationByScheduleId(
            @RequestParam("scheduleId") Long scheduleId) {
        try {
            Map<String, Object> response = scheduleService.getRouteByScheduleId(scheduleId);

            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .code(HttpStatus.OK.value())
                    .data(response)
                    .message("")
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorType.internalServerError);
        }
    }

    @PostMapping("getSchedules")
    public ResponseEntity<?> getSchedules(@RequestBody DataSchedule dataSchedule){
        try {
//            this.scheduleService.getSchedules(dataSchedule);
            return ResponseEntity.ok(ApiResponse.<List<ScheduleResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .data(this.scheduleService.getSchedules(dataSchedule))
                    .message("")
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorType.internalServerError);
        }
    }

    @GetMapping("/getByTime") // need review and edit sql and data type LocalDateTime
    public ResponseEntity<?> getScheduleByTime(
            @RequestParam("departureTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime departureTime,
            @RequestParam("returnTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime returnTime,
            @RequestParam("originalLocation") String originalLocation,
            @RequestParam("destination") String destination) {
        try {
            Map<String, Object> response = scheduleService.getAllScheduleByTime(departureTime, returnTime, originalLocation, destination);

            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .code(HttpStatus.OK.value())
                    .data(response)
                    .message("")
                    .build());
        } catch (DateTimeParseException e) {
            log.error(e.getMessage());
            throw new AppException(ErrorType.notValidDateFormat);
        } catch (AppException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorType.internalServerError);
        }
    }

    @GetMapping("/getSamePrice")
    public ResponseEntity<?> getScheduleSamePrice(
            @RequestParam("price") double price,
            @RequestParam("originalLocation") String originalLocation,
            @RequestParam("destination") String destination,
            @RequestParam("departureDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate departureDate) {
        try {
            List<Map<String, Object>> response = scheduleService.getScheduleSamePrice(price, originalLocation, destination, departureDate);

            return ResponseEntity.ok(ApiResponse.<List<Map<String, Object>>>builder()
                    .code(HttpStatus.OK.value())
                    .data(response)
                    .message("")
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorType.internalServerError);
        }
    }

    @GetMapping("getByEnterpriseId")
    public ResponseEntity<?> getByEnterpriseId(){
        try {
            return ResponseEntity.ok(ApiResponse.<List<ScheduleResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .data(scheduleService.getScheduleByEnterpriseId())
                    .message("")
                    .build());
        } catch (Exception e){
            return null;
        }
    }

    @GetMapping("getSeatsByScheduleId")
    public ResponseEntity<?> getSeatsByScheduleId(
            @RequestParam("scheduleId") Integer scheduleId) {
        try {
            Map<String, Object> response = scheduleService.getSeatsByScheduleId(scheduleId);

            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .code(HttpStatus.OK.value())
                    .data(response)
                    .message("")
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorType.internalServerError);
        }
    }
}
