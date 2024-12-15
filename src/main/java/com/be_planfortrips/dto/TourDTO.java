package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDTO {
    String title;
    @JsonProperty("route_id")
    String routeId;
    @JsonProperty("number_people")
    Integer numberPeople;
    @JsonProperty("total_price")
    Double totalPrice;
    Integer day;
    Integer night;
    @JsonProperty("is_active")
    Boolean isActive;
    List<String> tagNames;
    String note;
    @JsonProperty("hotel_id")
    Long hotelId;
    @JsonProperty("car_company_id")
    Integer carCompanyId;
    @JsonProperty("checkin_id")
    List<Long> checkinId;
    @JsonProperty("admin_username")
    String adminUsername;
}
