package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.VnPayDTO;
import com.be_planfortrips.dto.response.VnpPayResponse;
import com.be_planfortrips.services.interfaces.IVnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public String returnPage(@RequestParam Map<String, String> requestParams) throws IOException {
        return iVnPayService.returnPage(requestParams);
    }
}
