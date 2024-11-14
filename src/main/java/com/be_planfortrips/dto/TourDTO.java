package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDTO {
    String title;
    String description;
    String destination;
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
    @JsonProperty("schedule_id")
    Integer scheduleId;
    @JsonProperty("admin_username")
    String adminUsername;
}
