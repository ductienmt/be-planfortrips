package com.be_planfortrips.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountEnterpriseDto {

//    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    String username;


//    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    String email;

//    @NotEmpty(message = "Enterprise name cannot be empty")
    String enterpriseName;

//    @NotEmpty(message = "Representative name cannot be empty")
    String representative;

//    @NotEmpty(message = "Tax code cannot be empty")
//    @Pattern(regexp = "\\d{3}", message = "Tax code must be 10 digits")
    String taxCode;

//    @NotEmpty(message = "Phone number cannot be empty")
//    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 and 15 digits")
    String phoneNumber;

//    @NotEmpty(message = "Address cannot be empty")
    String address;

//    @NotNull(message = "Status cannot be null")
//    Boolean status = false;

//    @NotNull(message = "Image ID cannot be null")
//    Integer imageId;

//    @NotEmpty(message = "City ID cannot be empty")
    String cityId;

//    @NotNull(message = "Type Enterprise Detail ID cannot be null")
    Long typeEnterpriseDetailId;
}
