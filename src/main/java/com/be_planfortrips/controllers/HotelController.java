package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.responses.HotelListResponse;
import com.be_planfortrips.responses.HotelResponse;
import com.be_planfortrips.services.interfaces.IHotelService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/hotels")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class HotelController {
    IHotelService iHotelService;

    @PostMapping("")
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
    @GetMapping("")
    public ResponseEntity<HotelListResponse> getHotels(@RequestParam("page")     int page,
                                                       @RequestParam("limit")    int limit){
        PageRequest request = PageRequest.of(page, limit,
                Sort.by("rating").ascending());
        int totalPage = 0;
        Page<HotelResponse> hotelResponses = iHotelService.getAllHotel(request);
        totalPage = hotelResponses.getTotalPages();
        return ResponseEntity.ok(HotelListResponse.builder()
                        .hotelResponseList(hotelResponses.toList())
                        .totalPage(totalPage)
                        .build());
    }
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long id) throws Exception {
        iHotelService.deleteHotelById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Long id){
        try {
            HotelResponse hotelResponse = iHotelService.getByHotelId(id);
            return ResponseEntity.ok(hotelResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
