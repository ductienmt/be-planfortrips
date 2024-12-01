package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.response.AccountEnterpriseResponse;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.services.interfaces.IAccountEnterpriseService;
import com.be_planfortrips.utils.Utils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
    @RequestMapping("${api.prefix}/account-enterprises")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountEnterpriseController {

    IAccountEnterpriseService accountEnterpriseService;

    @GetMapping("all")
    public ResponseEntity<List<AccountEnterpriseResponse>> getAllAccountEnterprises() {
        List<AccountEnterpriseResponse> accountEnterprises = accountEnterpriseService.getAllAccountEnterprises(1, 30);
        return new ResponseEntity<>(accountEnterprises, HttpStatus.OK);
    }

    @GetMapping("/accept")
    public ResponseEntity<List<AccountEnterpriseResponse>> getEnterpriseNeedAccept() {
        List<AccountEnterpriseResponse> accountEnterpriseResponses = accountEnterpriseService.getAccountEnterpriseDisable();
        return ResponseEntity.ok(accountEnterpriseResponses);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<AccountEnterpriseResponse> getAccountEnterpriseById(@PathVariable Long id) {
        AccountEnterpriseResponse accountEnterpriseResponse = accountEnterpriseService.getAccountEnterpriseById(id);
        return new ResponseEntity<>(accountEnterpriseResponse, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<AccountEnterpriseResponse> createAccountEnterprise(
            @RequestBody @Valid  AccountEnterpriseDto accountEnterpriseDto) {
        AccountEnterpriseResponse accountEnterpriseResponse = accountEnterpriseService.createAccountEnterprise(accountEnterpriseDto);
        return new ResponseEntity<>(accountEnterpriseResponse, HttpStatus.CREATED);
    }

    @PutMapping("update")
    public ResponseEntity<AccountEnterpriseResponse> updateAccountEnterprise(
            @RequestBody AccountEnterpriseDto accountEnterpriseDto) {
        AccountEnterpriseResponse accountEnterpriseResponse = accountEnterpriseService.updateAccountEnterprise(accountEnterpriseDto);
        return new ResponseEntity<>(accountEnterpriseResponse, HttpStatus.OK);
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<Void> deleteAccountEnterpriseById(@PathVariable Long id) {
        accountEnterpriseService.deleteAccountEnterpriseById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

      @GetMapping("/detail")
    public ResponseEntity<AccountEnterpriseResponse> getAccountEnterpriseDetail() {
        AccountEnterpriseResponse accountEnterpriseResponse = accountEnterpriseService.getAccountEnterpriseDetail();
        return new ResponseEntity<>(accountEnterpriseResponse, HttpStatus.OK);
    }

    @PatchMapping("/stage/{id}")
    public ResponseEntity<Boolean> toggleStage(
            @PathVariable Long id
    )  {
        return ResponseEntity.ok(accountEnterpriseService.toggleStage(id));
    }

    @PostMapping("validate-username")
    public ResponseEntity<?> validateUsername(@RequestParam String username) {
        try {
            accountEnterpriseService.validateUsername(username);
            return ResponseEntity.ok(Map.of("message", "Tên đăng nhập hợp lệ."));
        } catch (AppException e) {
            return ResponseEntity.badRequest().body(Map.of(
//                    "error", e.getErrorType().name(),
                    "message", e.getMessage()
            ));
        }
    }
    @PostMapping("validate-email")
    public ResponseEntity<?> validateEmail(@RequestParam String email) {
        try {
            accountEnterpriseService.validateEmail(email);
            return ResponseEntity.ok(Map.of("message", "Email hợp lệ."));
        } catch (AppException e) {
            return ResponseEntity.badRequest().body(Map.of(
//                    "error", e.getErrorType().name(),
                    "message", e.getMessage()
            ));
        }
    }
    @PostMapping("validate-phone")
    public ResponseEntity<?> validatePhone(@RequestParam String phone) {
        try {
            if(!Utils.isValidPhoneNumber(phone)){
                throw new AppException(ErrorType.phoneNotValid);
            }
            accountEnterpriseService.validatePhone(phone);
            return ResponseEntity.ok(Map.of("message", "Phone hợp lệ."));
        } catch (AppException e) {
            return ResponseEntity.badRequest().body(Map.of(
//                    "error", e.getErrorType().name(),
                    "message", e.getMessage()
            ));
        }
    }

}
