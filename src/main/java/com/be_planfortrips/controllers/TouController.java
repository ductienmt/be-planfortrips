package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.TourDto;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.dto.response.rsTourResponse.TourScheduleBringData;
import com.be_planfortrips.dto.response.rsTourResponse.TourScheduleResponse;
import com.be_planfortrips.services.interfaces.ITourService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/tours/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TouController {

    ITourService tourService;

    @GetMapping("/all")
    public ResponseEntity<List<TourResponse>> getAlTour(
            @RequestParam(value = "page",required = false) Integer page,
            @RequestParam(value = "size",required = false) Integer size
    ) {
        List<TourResponse> responses = tourService.getAllTour(page, size);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/getById/{tourId}")
    public ResponseEntity<TourResponse> getTourById(
            @PathVariable Long tourId
    )  {
        TourResponse response = tourService.getTourById(tourId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<TourResponse> createTour(
            @RequestBody TourDto tourDto
            ) {
        TourResponse response = tourService.createTour(tourDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{tourId}")
    public ResponseEntity<Void> deleteTour(
            @PathVariable Long tourId
    ) {
        tourService.removeTourById(tourId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{tourId}")
    public ResponseEntity<TourResponse> updateTour(
            @PathVariable Long tourId,
            @RequestBody TourDto tourDto
    ) {
        TourResponse response = tourService.updateTourById(tourId, tourDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/schedule/")
    public ResponseEntity<List<TourScheduleResponse>> getScheduleEnable(
            @RequestParam("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime day,
            @RequestParam("cityId") String cityId
    ) {
    List<TourScheduleResponse> scheduleBringData = tourService.getScheduleAvailable(day, cityId);
    return ResponseEntity.ok(scheduleBringData);
    }


}
