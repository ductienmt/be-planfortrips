package com.be_planfortrips.dto.response;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelListResponse {
    List<HotelResponse> hotelResponseList;
    int totalPage;
}
