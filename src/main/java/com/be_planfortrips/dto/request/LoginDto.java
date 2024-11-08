package com.be_planfortrips.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginDto {
    @NotBlank(message = "Vui lòng nhập username")
    String userName;
    @NotBlank(message = "Vui lòng nhập password")
    String password;
}
