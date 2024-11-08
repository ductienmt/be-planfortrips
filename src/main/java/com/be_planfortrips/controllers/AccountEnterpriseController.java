package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.response.AccountEnterpriseResponse;
import com.be_planfortrips.services.interfaces.IAccountEnterpriseService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/account-enterprises")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountEnterpriseController {

    IAccountEnterpriseService accountEnterpriseService;

    @GetMapping("all")
    public ResponseEntity<List<AccountEnterpriseResponse>> getAllAccountEnterprises() {
        List<AccountEnterpriseResponse> accountEnterprises = accountEnterpriseService.getAllAccountEnterprises();
        return new ResponseEntity<>(accountEnterprises, HttpStatus.OK);
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

    @PostMapping("/change-status")
    public ResponseEntity<Void> changeStatus(@RequestParam Long id, @RequestParam Integer status) {
        accountEnterpriseService.changeStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
