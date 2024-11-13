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
    Double rating;
    @JsonProperty("total_price")
    Double totalPrice;
    Integer day;
    Integer night;
    @JsonProperty("is_active")
    boolean isActive;
    List<String> tagNames;
}
