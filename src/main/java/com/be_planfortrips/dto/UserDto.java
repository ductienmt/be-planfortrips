package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String userName;
    String password;
    String phoneNumber;
    String gender;
    String fullName;
    String email;
    String address;
    Date birthdate;
}
