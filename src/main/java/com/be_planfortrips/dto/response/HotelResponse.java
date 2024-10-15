package com.be_planfortrips.dto.response;
import com.be_planfortrips.entity.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelResponse {
    @JsonProperty("hotel_id")
    Long id;
    String name;
    String address;
    String phoneNumber;
    String description;
    int rating;
    List<Image> images ;
}
