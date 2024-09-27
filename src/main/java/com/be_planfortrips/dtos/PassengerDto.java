package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengerDto {
    String passengerName;
    Date birthDate;
    String gender;
    Integer citizenCardId;
    String phoneNumber;
    String email;
    Integer bookingId;
}
