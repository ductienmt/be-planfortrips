package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.response.ScheduleResponse;
import com.be_planfortrips.services.interfaces.IScheduleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/schedules")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleController {

    IScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
        List<ScheduleResponse> responses = scheduleService.getAllSchedule();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getScheduleById(
            @PathVariable Integer scheduleId) {
        ScheduleResponse response = scheduleService.getScheduleById(scheduleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(
            @RequestBody ScheduleDto scheduleDto) {
        ScheduleResponse response = scheduleService.createSchedule(scheduleDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable Integer scheduleId, @RequestBody ScheduleDto scheduleDto) {
        ScheduleResponse response = scheduleService.updateSchedule(scheduleDto, scheduleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteScheduleById(
            @PathVariable Integer scheduleId) {
        scheduleService.deleteScheduleById(scheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @GetMapping("/getByTime")
//    public ResponseEntity<?> getScheduleByTime(@RequestParam("departureTime") LocalDateTime departureTime, @RequestParam("arrivalTime") LocalDateTime arrivalTime) {
//        List<ScheduleResponse> responses = scheduleService.getAllScheduleByTime(departureTime, arrivalTime);
//        return new ResponseEntity<>(responses, HttpStatus.OK);
//    } need review and edit sql and data type LocalDateTime, need to be changed to "yyyy-MM-dd HH:mm:ss"
}
