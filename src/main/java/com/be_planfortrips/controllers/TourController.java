package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.TourDTO;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.dto.response.TourClientResponse;
import com.be_planfortrips.dto.response.TourDetailResponse;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.services.interfaces.ITourService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            Page<TourResponse> tourResponses = iTourService.getActiveTours(request,titleName,rating,tags);
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
            if(tags.size() > 0) tourDTO.setTagNames(tags);
            TourResponse tourResponse = iTourService.createTour(tourDTO);
            return ResponseEntity.ok(tourResponse);
        }catch (Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
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
    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable Integer id,@RequestPart("files") List<MultipartFile> files)throws IllegalArgumentException{
        try{
            TourResponse response = iTourService.uploadImage(id,files);
            return ResponseEntity.ok(response);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @DeleteMapping("deleteImages/{id}")
    public ResponseEntity<?> deleteImages(@PathVariable Integer id,
                                          @RequestParam String imageIds){
        try {
            List<Integer> imageIdsList = Arrays.stream(imageIds.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            TourResponse response = iTourService.deleteImage(id,imageIdsList);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/available/{tourId}")
    public ResponseEntity<?> getTourAvailable(
            @PathVariable Integer tourId
    ) {
        TourDetailResponse tourDetailResponse = iTourService.getTourDetail(tourId);
        return ResponseEntity.ok(tourDetailResponse);
    }



    @GetMapping("/client")
    public ResponseEntity<?> getAllTourClient() {
        List<TourClientResponse> responses = iTourService.getAllTourClient();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/route/{cityOriginId}/{cityDesId}")
    public ResponseEntity<?> getTourByDestination(
            @PathVariable String cityDesId,
            @PathVariable String cityOriginId
    ) {
        List<TourClientResponse> res = iTourService.getTourByDestination(cityDesId, cityOriginId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/destination/{cityId}")
    public ResponseEntity<?> getTourHasDestination(
            @PathVariable String cityId
    ) {
        List<Map<String, Object>> tourClientResponses = iTourService.getTourHasDestination(cityId);
        return ResponseEntity.ok(tourClientResponses);
    }

//    @GetMapping("/checkIn/{checkInId}")
//    public ResponseEntity<?> getTourHasCheckIn(
//            @PathVariable Integer checkInId
//    ) {
//        List<Map<String, Object>> tourClientResponses = iTourService.getTourHasCheckIn(checkInId);
//        return ResponseEntity.ok(tourClientResponses);
//    }

    @GetMapping("/area/{areaId}")
    public ResponseEntity<?> getTourHaveCityIn(
            @PathVariable String areaId
    ) {
        List<Map<String, Object>> tourClientResponses = iTourService.getTourHaveCityIn(areaId);
        return ResponseEntity.ok(tourClientResponses);
    }



    @GetMapping("/top1")
    public ResponseEntity<?> getTourTop1Used() {
        List<Map<String, Object>> response = iTourService.getTourTopUsed();
        return ResponseEntity.ok(response);
    }


}
