package com.be_planfortrips.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CityDto {
    @NotBlank(message = "Vui lòng nhập id.")
    String areaId;
    @NotBlank(message = "Vui lòng nhập tên thành phố.")
    String nameCity;
    String description;
}
