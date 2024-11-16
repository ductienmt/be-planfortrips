package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.TourDTO;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.dto.response.TagResponse;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.dto.response.rsTourResponse.TourScheduleResponse;
import com.be_planfortrips.services.interfaces.ITourService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/tours")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TourController {
    ITourService iTourService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllTours(@RequestParam("page") int page,
                                         @RequestParam("limit") int limit,
                                         @RequestParam(value = "titleName",required = false) String titleName,
                                         @RequestParam(value = "rating",required = false) Integer rating,
                                         @RequestParam(value = "tagNames",required = false) String tagNames){
        try {
            PageRequest request = PageRequest.of(page,limit);
            int totalPage = 0;
            List<String> tags = tagNames!=null?Arrays.stream(tagNames.split(",")).toList():new ArrayList<>();
            Page<TourResponse> tourResponses = iTourService.getTours(request,titleName,rating,tags);
            totalPage = tourResponses.getTotalPages();
            TListResponse<TourResponse> listResponse = new TListResponse<>();
            listResponse.setTotalPage(totalPage);
            listResponse.setListResponse(tourResponses.stream().toList());
            return ResponseEntity.ok(listResponse);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> createTour(@RequestBody TourDTO tourDTO,
                                        @RequestParam(value = "tagNames",required = false) String tagNames,
                                        BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            List<String> tags = tagNames!=null?Arrays.stream(tagNames.split(",")).toList():new ArrayList<>();
            if(tags.size() > 0)            tourDTO.setTagNames(tags);
            TourResponse tourResponse = iTourService.createTour(tourDTO);
            return ResponseEntity.ok(tourResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("update/{id}")
    public ResponseEntity<?> updateTour(@PathVariable("id") Integer id,
                                        @RequestBody TourDTO tourDTO,
                                        @RequestParam(value = "tagNames",required = false) String tagNames,
                                        BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            List<String> tags = tagNames!=null?Arrays.stream(tagNames.split(",")).toList():new ArrayList<>();
            if(tags.size() > 0)            tourDTO.setTagNames(tags);
            TourResponse tourResponse = iTourService.updateTour(id,tourDTO);
            return ResponseEntity.ok(tourResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteTour(@PathVariable("id") Integer id){
        try {
            iTourService.deleteTourById(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("findById/{id}")
    public ResponseEntity<?> getTourById(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok(iTourService.getByTourId(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/schedule/")
    public ResponseEntity<List<TourScheduleResponse>> getScheduleEnable(
            @RequestParam("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime day,
            @RequestParam("cityId") String cityId
    ) {
        List<TourScheduleResponse> scheduleBringData = iTourService.getScheduleAvailable(day, cityId);
        return ResponseEntity.ok(scheduleBringData);
    }



}
