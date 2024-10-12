package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.ScheduleDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.ScheduleResponse;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.services.interfaces.IScheduleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getByTime") //    need review and edit sql and data type LocalDateTime
    public ResponseEntity<?> getScheduleByTime(
            @RequestParam("departureTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime departureTime,
            @RequestParam("returnTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime returnTime) {
        try {
            Map<String, Object> response = scheduleService.getAllScheduleByTime(departureTime, returnTime);

            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .code(HttpStatus.OK.value())
                    .data(response)
                    .message("")
                    .build());
        } catch (DateTimeParseException e) {
            log.error(e.getMessage());
            // Xử lý lỗi định dạng ngày giờ và ném ngoại lệ tùy chỉnh
            throw new AppException(ErrorType.notValidDateFormat);
        } catch (AppException e) {
            log.error(e.getMessage());
            // Ném lại AppException nếu có
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            // Bắt mọi lỗi khác và trả về Internal Server Error
            throw new AppException(ErrorType.internalServerError);
        }
    }


}
