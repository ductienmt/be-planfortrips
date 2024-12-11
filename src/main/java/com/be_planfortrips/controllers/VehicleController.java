package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.VehicleDTO;
import com.be_planfortrips.dto.response.CarResponse;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.dto.response.VehicleResponse;
import com.be_planfortrips.services.interfaces.IVehicleService;
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
@RequestMapping("${api.prefix}/vehicles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class VehicleController {

    IVehicleService iVehicleService;

    @PostMapping("/create")
    public ResponseEntity<?> createVehicle(@RequestBody @Valid VehicleDTO vehicleDTO,
                                              BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            VehicleResponse vehicleResponse = iVehicleService.createVehicle(vehicleDTO);
            return ResponseEntity.ok(vehicleResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update/{code}")
    public ResponseEntity<?> updateVehicle(@PathVariable String code,@RequestBody @Valid VehicleDTO vehicleDTO,
                                              BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            VehicleResponse vehicleResponse = iVehicleService.updateVehicle(code,vehicleDTO);
            return ResponseEntity.ok(vehicleResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("all")
    public ResponseEntity<?> getCarCompanies(@RequestParam int page,
                                             @RequestParam int limit,
                                             @RequestParam(defaultValue = "") String keyword){
        try {
            PageRequest request = PageRequest.of(page,limit, Sort.by("code").ascending());
            int totalPage = 0;
            Page<VehicleResponse> vehicleResponses = iVehicleService.getVehicles(request,keyword);
            totalPage = vehicleResponses.getTotalPages();
            TListResponse<VehicleResponse> listResponse= new TListResponse<>();
            listResponse.setListResponse(vehicleResponses.toList());
            listResponse.setTotalPage(totalPage);
            return ResponseEntity.ok(listResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{code}")
    public ResponseEntity<?> deleteCarCompanyById(@PathVariable String code) throws Exception {
        iVehicleService.deleteVehicleById(code);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("getById/{code}")
    public ResponseEntity<?> getCarCompanyById(@PathVariable String code){
        try {
            VehicleResponse response = iVehicleService.getByVehicleId(code);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("getByEnterpriseId")
    public ResponseEntity<?> getByEnterpriseId() {
        try {
            return ResponseEntity.ok(this.iVehicleService.getVehiclesByEnterpriseId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
