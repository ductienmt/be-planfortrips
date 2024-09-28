package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String username;
    String password;
    String phoneNumber;
    String gender;
    String fullName;
    String email;
    String address;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    Boolean isActive;
    Date birthdate;
    Integer isFacebook;
    Integer isGoogle;
    Integer imageId;
}
