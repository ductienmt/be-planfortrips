package com.be_planfortrips.dto;

import lombok.Data;

@Data
public class SocialAccountDto {
    private String provider;
    private String providerId;
    private Integer userId;
}
