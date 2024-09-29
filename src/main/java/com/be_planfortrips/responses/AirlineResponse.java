package com.be_planfortrips.responses;
import com.be_planfortrips.entity.Airplane;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    List<Airplane> airplanes;
}
