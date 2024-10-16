package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteDTO {
    @JsonProperty("route_id")
    @NotBlank(message = "route_id is required")
    String id;
    @JsonProperty("origin_station_id")
    @NotNull(message = "origin_station_id is required")
    @Min(value = 1,message = "origin_station_id must be greater than 1!")
    Integer originStationId;
    @JsonProperty("destination_station_id")
    @NotNull(message = "destination_station_id is required")
    @Min(value = 1,message = "destination_station_id must be greater than 1!")
    Integer destinationStationId;
}

