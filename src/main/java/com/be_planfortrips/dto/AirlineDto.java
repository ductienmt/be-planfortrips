package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirlineDto {
    @NotBlank(message = "Airline name is required")
    @Length(max = 100,message = "Airline name must be less than 100 character")
    @JsonProperty("airline_name")
    String airlineName;
    @NotBlank(message = "Airline code is required")
    @Length(max = 6,message = "Airline code must be less than 6 character")
    @JsonProperty("airline_code")
    String airlineCode;
    @NotBlank(message = "Airline country is required")
    @JsonProperty("airline_country")
    String airlineCountry;
    @NotBlank(message = "Enterprise is required")
    @Min(value = 1,message = "enterprise id must be greater than 1")
    @JsonProperty("enterprise_id")
    Integer enterpriseId;
}
