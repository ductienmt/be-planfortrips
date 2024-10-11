package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.response.AccountEnterpriseResponse;
import com.be_planfortrips.services.interfaces.IAccountEnterpriseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/account-enterprises")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountEnterpriseController {

    IAccountEnterpriseService accountEnterpriseService;

    @GetMapping
    public ResponseEntity<List<AccountEnterpriseResponse>> getAllAccountEnterprises() {
        List<AccountEnterpriseResponse> accountEnterprises = accountEnterpriseService.getAllAccountEnterprises();
        return new ResponseEntity<>(accountEnterprises, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountEnterpriseResponse> getAccountEnterpriseById(@PathVariable Long id) {
        AccountEnterpriseResponse accountEnterpriseResponse = accountEnterpriseService.getAccountEnterpriseById(id);
        return new ResponseEntity<>(accountEnterpriseResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountEnterpriseResponse> createAccountEnterprise(@RequestBody AccountEnterpriseDto accountEnterpriseDto) {
        AccountEnterpriseResponse accountEnterpriseResponse = accountEnterpriseService.createAccountEnterprise(accountEnterpriseDto);
        return new ResponseEntity<>(accountEnterpriseResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountEnterpriseResponse> updateAccountEnterprise(
            @PathVariable Long id,
            @RequestBody AccountEnterpriseDto accountEnterpriseDto) {
        AccountEnterpriseResponse accountEnterpriseResponse = accountEnterpriseService.updateAccountEnterprise(id, accountEnterpriseDto);
        return new ResponseEntity<>(accountEnterpriseResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountEnterpriseById(@PathVariable Long id) {
        accountEnterpriseService.deleteAccountEnterpriseById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
