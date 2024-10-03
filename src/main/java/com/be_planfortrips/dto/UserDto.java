package com.be_planfortrips.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotBlank(message = "Vui lòng nhập username")
    String userName;
    @NotBlank(message = "Vui lòng nhập password")
    String password;
    @NotBlank(message = "Vui lòng nhập số điện thoại")
    String phoneNumber;
    @NotBlank(message = "Vui lòng nhập giới tính")
    String gender;
    @NotBlank(message = "Vui lòng nhập họ và tên")
    String fullName;
    @NotBlank(message = "Vui lòng nhập email")
    String email;
    String address;
    @NotNull(message = "Vui lòng nhập ngày sinh")
    Date birthdate;
}
