package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.VnPayDTO;
import com.be_planfortrips.dto.response.VnpPayResponse;
import com.be_planfortrips.services.interfaces.IVnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.*;


@RestController
@RequestMapping("${api.prefix}/payments/vnpay")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class VnPayController {

    IVnPayService iVnPayService;

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(
            @Valid @RequestBody VnPayDTO vnPayDTO,
            BindingResult result,
            HttpServletRequest request){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            VnpPayResponse vnpPayResponse = iVnPayService.createPayment(vnPayDTO,request);
            return ResponseEntity.ok(vnpPayResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/return")
    public ResponseEntity<?> returnPage(@RequestParam Map<String, String> requestParams) throws IOException {
        String returnValue= iVnPayService.returnPage(requestParams);
        if(returnValue.equals("00")){
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:5050/user/success"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }else{
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:5050/user/fail"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }
    }
    @PostMapping("/create-payment-plan")
    public ResponseEntity<?> createPaymentForPlan(
            @Valid @RequestParam("plan_id") Integer id,
            HttpServletRequest request){
        try {
            VnpPayResponse vnpPayResponse = iVnPayService.createPaymentForPlan(id,request);
            return ResponseEntity.ok(vnpPayResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/return-plan")
    public ResponseEntity<?> returnPageForPlan(@RequestParam Map<String, String> requestParams) throws IOException {
        String returnValue=  iVnPayService.returnPageForPlan(requestParams);
        if(returnValue.equals("00")){
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:5050/user/success"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }else{
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:5050/user/fail"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }
    }
}
