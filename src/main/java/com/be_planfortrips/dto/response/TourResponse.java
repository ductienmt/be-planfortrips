package com.be_planfortrips.dto.response;

import com.be_planfortrips.dto.response.rsTourResponse.TourCarCompanyResponse;
import com.be_planfortrips.dto.response.rsTourResponse.TourCityResponse;
import com.be_planfortrips.dto.response.rsTourResponse.TourHotelResponse;
import com.be_planfortrips.dto.response.rsTourResponse.TourTagResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourResponse {

    Long tourId;

    String title;
    Integer day;
    Integer night;
    Boolean isActive;
    Double rating;
    Integer numberPeople;
    Double totalPrice;

    TourCarCompanyResponse carCompany;
    TourHotelResponse hotel;

    TourCityResponse destination;
    TourCityResponse departure;

    List<TourTagResponse> tags;


}
