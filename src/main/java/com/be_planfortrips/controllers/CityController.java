package com.be_planfortrips.controllers;

import com.be_planfortrips.services.interfaces.ICityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/cities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CityController {
    ICityService iCityService;
    @GetMapping("all")
    public ResponseEntity<?> getCityByAreaId(@RequestParam("area_id") String area_id){
        try {
            return ResponseEntity.ok(iCityService.getCityByAreaId(area_id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
