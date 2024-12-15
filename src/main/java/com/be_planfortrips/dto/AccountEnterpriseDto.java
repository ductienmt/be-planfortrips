package com.be_planfortrips.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountEnterpriseDto {
//    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    String username;
    @Email(message = "Email should be valid")
    String email;
    String enterpriseName;
    String representative;
    String taxCode;
    String phoneNumber;
    String address;
    String cityId;
    Long typeEnterpriseDetailId;
}
