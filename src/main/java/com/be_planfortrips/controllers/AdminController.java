package com.be_planfortrips.controllers;

import com.be_planfortrips.entity.Admin;
import com.be_planfortrips.services.interfaces.IAdminService;
import com.be_planfortrips.services.interfaces.IAuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/admins")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AdminController {
    IAdminService adminService;
    @GetMapping("findByUserName") //
        public ResponseEntity<?> findAdminByUsername(@RequestParam("userName") String username){
        try {
            Admin admin = adminService.findAdminByUsername(username);
            return ResponseEntity.ok(admin);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
