package com.be_planfortrips.dto.response.rsTourResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourCityResponse {

    String cityId;
    String cityName;
}
