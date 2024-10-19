package com.be_planfortrips.controllers;


import com.be_planfortrips.dto.response.AuthResponse;
import com.be_planfortrips.services.interfaces.IAuth2Service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:5050/")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class Auth2Controller {
    IAuth2Service iAuth2Service;

   //  Get Auth2 Url
    @GetMapping("/google/url")
    public ResponseEntity<String> getGoogleAuthorizationUrl() {
        String url = iAuth2Service.getGoogleUrl();
        return ResponseEntity.ok(url);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<AuthResponse> callback(
            @RequestParam("code") String code
    ) throws IOException {
        AuthResponse response = iAuth2Service.loginGoogle(code);
        return ResponseEntity.ok(response);
    }


}
