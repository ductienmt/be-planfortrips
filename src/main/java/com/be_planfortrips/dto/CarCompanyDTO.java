package com.be_planfortrips.dto;

import com.be_planfortrips.entity.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarCompanyDTO {
    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    String name;
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    String phoneNumber;
    @JsonProperty("enterprise_id")
    @Min(value = 1,message = "Enterprise id must greater than 1")
    Integer enterpriseId;
    @JsonProperty("rating")
    @Min(value = 1,message = "rating must be greater than 1 star")
    @Max(value = 5,message = "rating must be less than 5 star")
    Integer rating;
}
