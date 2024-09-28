package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelDto {
    @JsonProperty("enterprise_id")
    Long enterpriseId;
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
    @JsonProperty("rating")
    @Min(1) @Max(5)
    int rating;
}
