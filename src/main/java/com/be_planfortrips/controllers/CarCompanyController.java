package com.be_planfortrips.controllers;
import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.response.CarResponse;
import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.services.interfaces.ICarCompanyService;
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
@RequestMapping("${api.prefix}/car-companies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CarCompanyController {
    ICarCompanyService iCarService;

    @PostMapping("/create")
    public ResponseEntity<?> createCarCompany(@RequestBody @Valid CarCompanyDTO carCompanyDTO,
                                              BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            CarResponse carResponse = iCarService.createCar(carCompanyDTO);
            return ResponseEntity.ok(carResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("update/{id}")
    public ResponseEntity<?> updateCarCompany(@PathVariable int id,@RequestBody @Valid CarCompanyDTO carCompanyDTO,
                                              BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            CarResponse carResponse = iCarService.updateCar(id,carCompanyDTO);
            return ResponseEntity.ok(carResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> getCarCompanies(@RequestParam int page,
                                             @RequestParam int limit){
        try {
            PageRequest request = PageRequest.of(page,limit, Sort.by("rating").ascending());
            int totalPage = 0;
            Page<CarResponse> carResponses = iCarService.getCars(request);
            totalPage = carResponses.getTotalPages();
            TListResponse<CarResponse> listResponse= new TListResponse<>();
            listResponse.setListResponse(carResponses.toList());
            listResponse.setTotalPage(totalPage);
            return ResponseEntity.ok(listResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteCarCompanyById(@PathVariable int id){
        iCarService.deleteCarById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getCarCompanyById(@PathVariable int id){
        try {
            CarResponse response = iCarService.getByCarId(id);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable Integer id,@RequestPart("files") List<MultipartFile> files)throws IllegalArgumentException{
        try{
            CarResponse response = iCarService.uploadImage(id,files);
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
            CarResponse response = iCarService.deleteImage(id,imageIdsList);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
