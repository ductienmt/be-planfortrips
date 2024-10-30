package com.be_planfortrips.dto;

import com.be_planfortrips.entity.TypeVehicle;
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
    String routeId;
    String vehicleCode;
    String driverName;
    String driverPhone;
    BigDecimal priceForOneSeat;
    LocalDateTime departureTime;
    LocalDateTime arrivalTime;
    List<SeatDTO> seat;

}
