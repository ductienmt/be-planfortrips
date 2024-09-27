package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {
    @JsonProperty("enterprise_id")
    private Long enterpriseId;
    @JsonProperty("address")
    @NotBlank(message = "Address is required")
    private String address;
    @JsonProperty("name")
    @NotBlank(message = "Name hotel is required")
    private String name;
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    @JsonProperty("description")
    private String description;
    @JsonProperty("rating")
    @Min(1) @Max(5)
    private int rating;
}
