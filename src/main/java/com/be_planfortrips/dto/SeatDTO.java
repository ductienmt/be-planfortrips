package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatDTO {
    @JsonProperty("vehicle_code")
    @NotBlank(message = "vehicle_code is required")
    String vehicleCode;
    @JsonProperty("seat_number")
    @NotBlank(message = "seat_number is required")
    String seatNumber;
}
