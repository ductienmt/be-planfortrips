package com.be_planfortrips.controllers;

import com.be_planfortrips.services.interfaces.ICityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${api.prefix}/cities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CityController {
    private static final Logger log = LoggerFactory.getLogger(CityController.class);
    ICityService iCityService;
    @GetMapping("all")
    public ResponseEntity<?> getCityByAreaId(@RequestParam("area_id") String area_id){
        try {
            return ResponseEntity.ok(iCityService.getCityByAreaId(area_id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("getAll")
    public ResponseEntity<?> getAllCity(){
        try {
            return ResponseEntity.ok(iCityService.getAllCity());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("getByAreaId")
    public ResponseEntity<?> getCitiesByAreaId(@RequestParam() String id){
        try {
            return ResponseEntity.ok(iCityService.getCitiesByAreaId(id));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("city_id") String city_id){
        try {
            iCityService.uploadImage(file, city_id);
            return ResponseEntity.ok().body("Upload ảnh thành công");
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
