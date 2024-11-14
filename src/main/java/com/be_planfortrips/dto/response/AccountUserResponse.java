package com.be_planfortrips.dto.response;


import com.be_planfortrips.entity.Image;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountUserResponse {
    Long id;
    String userName;
    String phoneNumber;
    String gender;
//    String password;
    String address;
    boolean isActive;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate birthdate;
    String fullName;
    String email;
    Image image;
}


