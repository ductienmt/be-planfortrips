package com.be_planfortrips.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PassengerDto {
    private Integer passengerId;
    private String fullname;
    private Date birthdate;
    private String gender;
    private Integer citizenCardId;
    private String phonenumber;
    private String email;
    private Integer bookingId;
}
