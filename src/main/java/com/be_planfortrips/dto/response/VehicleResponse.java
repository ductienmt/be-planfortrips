package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.CarCompany;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleResponse {
    String code;
    String plateNumber;
    Integer capacity;
    String driverName;
    String driverPhone;
    @JsonProperty("car_company")
    CarCompany carCompany;
    @JsonProperty("type_vehicle")
    String typeVehicle;
}
