package com.be_planfortrips.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDto {

    String title;
    String description;

    Integer numberPeople;
    Integer day;
    Integer night;
    Boolean isActive;
    String note;

    Integer carCompanyId;
    Long hotelId;
    String cityIdDestination;
    String cityIdDeparture;
    List<Long> checkInIds;


    List<String> tagNames;

}
