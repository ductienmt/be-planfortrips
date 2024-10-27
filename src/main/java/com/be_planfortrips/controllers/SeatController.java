package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.SeatDTO;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.dto.response.SeatResponse;
import com.be_planfortrips.services.interfaces.ISeatService;
import com.be_planfortrips.services.interfaces.ISeatService;
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
@RequestMapping("${api.prefix}/seats")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SeatController {

    ISeatService iSeatService;

    @PostMapping("/create")
    public ResponseEntity<?> createSeat(@RequestBody @Valid SeatDTO SeatDTO,
                                           BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            SeatResponse SeatResponse = iSeatService.createSeat(SeatDTO);
            return ResponseEntity.ok(SeatResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateSeat(@PathVariable Integer id,@RequestBody @Valid SeatDTO SeatDTO,
                                           BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            SeatResponse SeatResponse = iSeatService.updateSeat(id,SeatDTO);
            return ResponseEntity.ok(SeatResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("all")
    public ResponseEntity<?> getCarCompanies(@RequestParam int page,
                                             @RequestParam int limit){
        try {
            PageRequest request = PageRequest.of(page,limit, Sort.by("id").ascending());
            int totalPage = 0;
            Page<SeatResponse> SeatResponses = iSeatService.getSeats(request);
            totalPage = SeatResponses.getTotalPages();
            TListResponse<SeatResponse> listResponse= new TListResponse<>();
            listResponse.setListResponse(SeatResponses.toList());
            listResponse.setTotalPage(totalPage);
            return ResponseEntity.ok(listResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteCarCompanyById(@PathVariable Integer id) throws Exception {
        iSeatService.deleteSeatById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getCarCompanyById(@PathVariable Integer id){
        try {
            SeatResponse response = iSeatService.getBySeatId(id);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
