package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountEnterpriseDto {
    String username;
    String password;
    String email;
    String enterpriseName;
    String representative;
    String taxCode;
    String phoneNumber;
    String address;
    Boolean status;
    Integer imageId;
    LocalDateTime createAt;
    LocalDateTime updateAt;
}


