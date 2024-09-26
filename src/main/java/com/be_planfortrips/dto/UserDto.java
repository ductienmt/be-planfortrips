package com.be_planfortrips.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserDto {
    private Integer id;
    private String username;
    private String phonenumber;
    private String gender;
    private String fullname;
    private String email;
    private String address;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Boolean isActive;
    private Date birthdate;
    private Integer facebookAccountId;
    private Integer googleAccountId;
    private Integer imageId;
}
