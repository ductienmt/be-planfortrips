package com.be_planfortrips.dto.response.HotelResponses;

import com.be_planfortrips.dto.response.HotelResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListAvailableResponses {
    List<AvailableHotels> hotelResponseList;
    int totalPage;
}
