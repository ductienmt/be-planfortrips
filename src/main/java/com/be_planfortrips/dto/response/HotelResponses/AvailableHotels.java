package com.be_planfortrips.dto.response.HotelResponses;

import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.entity.HotelAmenities;
import com.be_planfortrips.entity.Image;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AvailableHotels {
    Long id;
    String name;
    String address;
    String phoneNumber;
    String description;
    int rating;
    List<Image> images ;
    String roomName;
    Long price;
    List<HotelAmenities>  hotelAmenities;
    public AvailableHotels(Long id, String name, String address, String phoneNumber,
                           String description, int rating, Image image,
                           String roomName, BigDecimal price,HotelAmenities hotelAmenitie) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.rating = rating;
        this.images = image != null ? List.of(image) : List.of(); // Wrap the image in a list
        this.roomName = roomName;
        this.price = price != null ? price.longValue() : null; // Convert BigDecimal to Long
        this.hotelAmenities = hotelAmenitie!= null ? List.of(hotelAmenitie) : List.of();
    }

}
