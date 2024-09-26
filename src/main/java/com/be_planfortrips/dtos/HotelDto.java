package com.be_planfortrips.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HotelDto {
    private Integer id;
    private Integer enterpriseId;
    private String address;
    private String phoneNumber;
    private String description;
    private String rating;
    private LocalDateTime createAt;
    private Integer imageId;
}
