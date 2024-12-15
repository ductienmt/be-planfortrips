package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.HotelAmenitiesResponse;
import com.be_planfortrips.dto.response.HotelListResponse;
import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.dto.response.HotelResponses.AvailableHotels;
import com.be_planfortrips.dto.response.HotelResponses.ListAvailableResponses;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.PageMapperImpl;
import com.be_planfortrips.services.interfaces.IHotelService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/hotels")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class HotelController {
    IHotelService iHotelService;
    private final PageMapperImpl pageMapperImpl;

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
    public ResponseEntity<HotelListResponse> getHotels(@RequestParam("page") int page,
                                                       @RequestParam("limit") int limit,
                                                       @RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "rating", required = false) Integer rating
                                                       ){
        PageRequest request = PageRequest.of(page, limit,
                Sort.by("createAt").ascending());
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

    @PostMapping("delete/{id}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long id) {
        iHotelService.deleteHotelById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Long id){
        try {
            HotelResponse hotelResponse = iHotelService.getByHotelId(id);
            System.out.println(hotelResponse.getHotelAmenities().stream().map(HotelAmenitiesResponse::getName).collect(Collectors.toList()));
            return ResponseEntity.ok(hotelResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("getByRoomId/{id}")
    public ResponseEntity<?> getHotelByRoomId(@PathVariable Long id){
        try {
            HotelResponse hotelResponse = iHotelService.getHotelByRoomId(id);
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
//    @PostMapping("/generate")
//    public ResponseEntity<String> stringResponseEntity(){
//        Faker faker = new Faker();
//        for(int i =0;i<100;i++){
//            HotelDto hotelDto =HotelDto.builder()
//                    .name(faker.name().name())
//                    .address(faker.address().fullAddress())
//                    .description(faker.lorem().sentence())
//                    .rating((int) faker.number().numberBetween(1,5))
//                    .phoneNumber(faker.phoneNumber().phoneNumber())
//                    .enterpriseId((long) faker.number().numberBetween(1,2))
//                    .build();
//
//            try {
//                iHotelService.createHotel(hotelDto);
//            } catch (Exception e) {
//                ResponseEntity.badRequest().body(e.getMessage());
//            }
//        }
//        return ResponseEntity.ok("Fake hotel created successfully");
//    }

    @GetMapping("detail")
    public ResponseEntity<?> getHotelDetail(){
        try {
            List<Map<String, Object>> hotelResponse = iHotelService.getHotelDetail();
            return ResponseEntity.ok(hotelResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("getByRouteId/{id}")
    public ResponseEntity<?> getHotelByRouteId(@PathVariable String id){
        try {
            return ResponseEntity.ok(iHotelService.getByRouteId(id));
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("findHotelAvailable")
    public ResponseEntity<?> findHotelAvailable(@RequestParam(value = "",required = false) String keyword,
                                                @RequestParam(value = "",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                @RequestParam(value = "",required = false) Long days,
                                                @RequestParam int page,
                                                @RequestParam int limit){
        try {
            PageRequest request = PageRequest.of(page,limit,Sort.by("name").descending());
            int totalPage = 0;
            LocalDateTime dateTime = date.atStartOfDay();
            Page<HotelResponse> hotelResponses = iHotelService.findHotelAvailable(request,keyword, dateTime, days);
            totalPage = hotelResponses.getTotalPages();
            System.out.println("So luong khach san tim dc: "+hotelResponses.toList().size());
            return ResponseEntity.ok(HotelListResponse.builder()
                    .hotelResponseList(hotelResponses.toList())
                    .totalPage(totalPage)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("getHotelsSamePrice")
    public ResponseEntity<?> getHotelsSamePrice(@RequestParam("price") double price, @RequestParam String destination){
        try {
            return ResponseEntity.ok(iHotelService.getHotelsSamePrice(price, destination));
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long id){
        try {
            iHotelService.changeStatus(id);
            return ResponseEntity.ok("Thay đổi trạng thái thành công");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("searchEnterprise")
    public ResponseEntity<?> searchEnterprise(@RequestParam(defaultValue = "", required = false) String keyword,
                                              @RequestParam(defaultValue = "0", required = false) int pageNo,
                                              @RequestParam(defaultValue = "10", required = false) int pageSize,
                                              @RequestParam(defaultValue = "id", required = false) String sortBy,
                                              @RequestParam(defaultValue = "asc", required = false) String sortType){

        try {
            var pageRequest = pageMapperImpl.customPage(pageNo, pageSize, sortBy, sortType);
            return ResponseEntity.ok(iHotelService.searchEnterprise(keyword, pageRequest));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
