package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.dto.response.HotelListResponse;
import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.services.interfaces.IHotelService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/hotels")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class HotelController {
    IHotelService iHotelService;
    @PostMapping("/create")
    public ResponseEntity<?> createHotel(@Valid @RequestBody HotelDto hotelDto,
                                         BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            HotelResponse hotelResponse = iHotelService.createHotel(hotelDto);
            return ResponseEntity.ok(hotelResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("error"+e.getMessage());
        }
    }
    @GetMapping("all")
    public ResponseEntity<HotelListResponse> getHotels(@RequestParam("page")     int page,
                                                       @RequestParam("limit")    int limit,
                                                       @RequestParam(defaultValue = "") String keyword,
                                                        @RequestParam(defaultValue = "") Integer rating
                                                       ){
        PageRequest request = PageRequest.of(page, limit,
                Sort.by("rating").ascending());
        int totalPage = 0;
        Page<HotelResponse> hotelResponses = iHotelService.searchHotels(request,keyword,rating);
        totalPage = hotelResponses.getTotalPages();
        return ResponseEntity.ok(HotelListResponse.builder()
                        .hotelResponseList(hotelResponses.toList())
                        .totalPage(totalPage)
                        .build());
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateHotel(@Valid @RequestBody HotelDto hotelDto,
                                         @PathVariable Long id,
                                         BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            HotelResponse updateHotel = iHotelService.updateHotel(id,hotelDto);
            return ResponseEntity.ok(updateHotel);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long id) {
        iHotelService.deleteHotelById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("getById/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Long id){
        try {
            HotelResponse hotelResponse = iHotelService.getByHotelId(id);
            return ResponseEntity.ok(hotelResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable long id,@RequestParam("files") List<MultipartFile> files)throws IllegalArgumentException{
        try{
            HotelResponse hotelResponse = iHotelService.createHotelImage(id,files);
            return ResponseEntity.ok(hotelResponse);
        }catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @DeleteMapping("deleteImages/{id}")
    public ResponseEntity<?> deleteImages(@PathVariable Long id,
                                          @RequestParam String imageIds){
        try {
            List<Integer> imageIdsList = Arrays.stream(imageIds.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            HotelResponse response = iHotelService.deleteImage(id,imageIdsList);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/generate")
    public ResponseEntity<String> stringResponseEntity(){
        Faker faker = new Faker();
        for(int i =0;i<100;i++){
            HotelDto hotelDto =HotelDto.builder()
                    .name(faker.name().name())
                    .address(faker.address().fullAddress())
                    .description(faker.lorem().sentence())
                    .rating((int) faker.number().numberBetween(1,5))
                    .phoneNumber(faker.phoneNumber().phoneNumber())
                    .enterpriseId((long) faker.number().numberBetween(1,2))
                    .build();

            try {
                iHotelService.createHotel(hotelDto);
            } catch (Exception e) {
                ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake hotel created successfully");
    }
}
