package com.be_planfortrips.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountEnterpriseDto {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String enterpriseName;
    private String representative;
    private String taxCode;
    private String phoneNumber;
    private String address;
    private Boolean status;
    private Integer imageId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
