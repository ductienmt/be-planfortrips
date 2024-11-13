package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.TourDTO;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.dto.response.TagResponse;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.services.interfaces.ITourService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> createTour(@RequestBody TourDTO tourDTO,
                                        @RequestParam String tagNames,
                                        BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            List<String> tags = Arrays.stream(tagNames.split(",")).toList();
            tourDTO.setTagNames(tags);
            TourResponse tourResponse = iTourService.createTour(tourDTO);
            return ResponseEntity.ok(tourResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}