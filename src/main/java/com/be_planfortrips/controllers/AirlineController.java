package com.be_planfortrips.controllers;
import com.be_planfortrips.dto.AirlineDto;
import com.be_planfortrips.dto.response.AirlineListResponse;
import com.be_planfortrips.dto.response.AirlineResponse;
import com.be_planfortrips.entity.Airline;
import com.be_planfortrips.services.interfaces.IAirlineService;
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
@RequestMapping("${api.prefix}/airlines")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AirlineController {
    IAirlineService iAirlineService;

    @PostMapping("")
    public ResponseEntity<?> saveAirline(@Valid @RequestBody AirlineDto airlineDto,
                                         BindingResult result){
       try {
           if(result.hasErrors()) {
               List<String> errorMessages = result.getFieldErrors()
                       .stream()
                       .map(FieldError::getDefaultMessage)
                       .toList();
               return ResponseEntity.badRequest().body(errorMessages);
           }
           Airline airline = iAirlineService.saveAirline(airlineDto);
           return ResponseEntity.ok(airline);
       }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @GetMapping("")
    public ResponseEntity<?> getAirlines(@RequestParam("page") int page,
                                         @RequestParam("limit") int limit){
        try {
            PageRequest request = PageRequest.of(page, limit,
                    Sort.by("id"));
            int totalPage = 0;
            Page<AirlineResponse> airlineResponses = iAirlineService.getAirlines(request);
            if(airlineResponses == null){
                return ResponseEntity.badRequest().body("Danh sách các chuyeens bay rỗng");
            }
            totalPage = airlineResponses.getTotalPages();
            return ResponseEntity.ok(AirlineListResponse.builder()
                    .list(airlineResponses.stream().toList())
                    .totalPage(totalPage)
                    .build()
                );
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAirline(@PathVariable Long id,
                                           @Valid @RequestBody AirlineDto airlineDto,
                                           BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Airline existingAirline = iAirlineService.updateAirline(id,airlineDto);
            return ResponseEntity.ok(existingAirline);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable Long id){
        iAirlineService.deleteAirlineById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getAirlineById(@PathVariable Long id){
        try {
            AirlineResponse airlineResponse = iAirlineService.getAirlineById(id);
            return ResponseEntity.ok(airlineResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
