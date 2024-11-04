package com.be_planfortrips.dto.request;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ChangePasswordDto {
    String oldPassword;
    String newPassword;
}
