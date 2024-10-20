package com.be_planfortrips.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocialLoginDto {
    @NotBlank(message = "Provider is required")
    private String provider;

    @NotBlank(message = "Token is required")
    private String token;
}
