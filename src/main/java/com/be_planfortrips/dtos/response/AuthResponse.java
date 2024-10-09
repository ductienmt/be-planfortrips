package com.be_planfortrips.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
    String userName;
    String phoneNumber;
    String fullName;
    String email;
//    String token;
}
