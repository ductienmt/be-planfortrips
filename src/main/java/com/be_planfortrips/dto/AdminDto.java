package com.be_planfortrips.dto;

import lombok.Data;

@Data
public class AdminDto {
    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private String email;
}
