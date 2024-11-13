package com.be_planfortrips.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleDto {

    @NotBlank(message = "Route ID cannot be blank")
    String routeId;

    @NotBlank(message = "Vehicle code cannot be blank")
    String vehicleCode;

    @NotBlank(message = "Driver name cannot be blank")
    @Size(min = 2, max = 50, message = "Driver name must be between 2 and 50 characters")
    String driverName;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Phone number is invalid")
    String driverPhone;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than zero")
    BigDecimal priceForOneSeat;

    @NotNull(message = "Departure time cannot be null")
    LocalDateTime departureTime;

    @NotNull(message = "Arrival time cannot be null")
    LocalDateTime arrivalTime;
}
