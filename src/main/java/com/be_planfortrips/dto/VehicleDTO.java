package com.be_planfortrips.dto;

import com.be_planfortrips.entity.TypeVehicle;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleDTO {
    @JsonProperty("code")
    @NotBlank(message = "code is required")
            @Length(max = 10,message = "Code must be less than 10")
    String code;
    @JsonProperty("plate_number")
    @NotBlank(message = "plate number is required")
    String plateNumber;
    @JsonProperty("capacity")
    @NotNull(message = "Capacity is required")
    @Min(value = 1,message = "Capacity must be greater than 1!")
    Integer capacity;
    @JsonProperty("driver_name")
    @NotBlank(message = "driverName is required")
    String driverName;
    @JsonProperty("driver_phone")
    @NotBlank(message = "driverPhone is required")
    String driverPhone;
    @JsonProperty("car_company_id")
    @NotNull(message = "car_company_id is required")
    @Min(value = 1,message = "car_company_id must be greater than 1!")
    Integer carCompanyId;
    @JsonProperty("type_vehicle")
    String typeVehicle;
}
