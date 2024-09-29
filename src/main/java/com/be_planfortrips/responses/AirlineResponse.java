package com.be_planfortrips.responses;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirlineResponse {
    String airlineName;
    String airlineCode;
    String airlineCountry;
    Long enterpriseId;
}
