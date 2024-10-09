package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelImageDto {
    @JsonProperty("hotel_id")
    Long hotelId;
    @JsonProperty("image_url")
    String imageUrl;
}
