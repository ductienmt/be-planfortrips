package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Station;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteResponse {
    @JsonProperty("route_id")
    String id;
    @JsonProperty("origin_station_id")
    Station originStation;
    @JsonProperty("destination_station_id")
    Station destinationStation;
}
