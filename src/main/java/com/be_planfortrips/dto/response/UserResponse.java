package com.be_planfortrips.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    Long id;
    String phoneNumber;
    String email;
    String gender;
    String address;
    Boolean isActive;
    LocalDate birthdate;
}
