//package com.be_planfortrips.controllers;
//import com.be_planfortrips.dto.AirlineDto;
//import com.be_planfortrips.dto.HotelDto;
//import com.be_planfortrips.dto.HotelImageDto;
//import com.be_planfortrips.entity.Airline;
//import com.be_planfortrips.entity.Hotel;
//import com.be_planfortrips.entity.HotelImage;
//import com.be_planfortrips.responses.*;
//import com.be_planfortrips.services.interfaces.IAirlineService;
//import com.be_planfortrips.services.interfaces.IHotelService;
//import jakarta.validation.Valid;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.core.io.UrlResource;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StringUtils;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("${api.prefix}/airlines")
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
//public class AirlineController {
//    IAirlineService iAirlineService;
//
//    @PostMapping("")
//    public ResponseEntity<?> saveAirline(@Valid @RequestBody AirlineDto airlineDto,
//                                         BindingResult result){
//       try {
//           if(result.hasErrors()) {
//               List<String> errorMessages = result.getFieldErrors()
//                       .stream()
//                       .map(FieldError::getDefaultMessage)
//                       .toList();
//               return ResponseEntity.badRequest().body(errorMessages);
//           }
//           Airline airline = iAirlineService.saveAirline(airlineDto);
//           return ResponseEntity.ok(airline);
//       }catch (Exception e){
//           return ResponseEntity.badRequest().body(e.getMessage());
//       }
//    }
//
//    @GetMapping("")
//    public ResponseEntity<?> getAirlines(@RequestParam("page") int page,
//                                         @RequestParam("limit") int limit){
//        try {
//            PageRequest request = PageRequest.of(page, limit,
//                    Sort.by("id"));
//            int totalPage = 0;
//            Page<AirlineResponse> airlineResponses = iAirlineService.getAirlines(request);
//            if(airlineResponses == null){
//                return ResponseEntity.badRequest().body("Danh sách các chuyeens bay rỗng");
//            }
//            totalPage = airlineResponses.getTotalPages();
//            return ResponseEntity.ok(AirlineListResponse.builder()
//                    .list(airlineResponses.stream().toList())
//                    .totalPage(totalPage)
//                    .build()
//                );
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateAirline(@PathVariable Long id,
//                                           @Valid @RequestBody AirlineDto airlineDto,
//                                           BindingResult result){
//        try {
//            if(result.hasErrors()) {
//                List<String> errorMessages = result.getFieldErrors()
//                        .stream()
//                        .map(FieldError::getDefaultMessage)
//                        .toList();
//                return ResponseEntity.badRequest().body(errorMessages);
//            }
//            Airline existingAirline = iAirlineService.updateAirline(id,airlineDto);
//            return ResponseEntity.ok(existingAirline);
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAirline(@PathVariable Long id){
//        iAirlineService.deleteAirlineById(id);
//        return ResponseEntity.noContent().build();
//    }
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getAirlineById(@PathVariable Long id){
//        try {
//            AirlineResponse airlineResponse = iAirlineService.getAirlineById(id);
//            return ResponseEntity.ok(airlineResponse);
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//}
