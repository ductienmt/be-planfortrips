package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelDto {
//    @JsonProperty("enterprise_id")
//    @Min(value = 1,message = "enterprise id must be greater than 1")
//    Long enterpriseId;
    @JsonProperty("address")
    @NotBlank(message = "Address is required")
    String address;
    @JsonProperty("name")
    @NotBlank(message = "Name hotel is required")
    String name;
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    String phoneNumber;
    @JsonProperty("description")
    String description;
//    @JsonProperty("rating")
//    @Min(value = 1,message = "rating must be greater than 1 star")
//    @Max(value = 5,message = "rating must be less than 5 star")
//    int rating;
}
