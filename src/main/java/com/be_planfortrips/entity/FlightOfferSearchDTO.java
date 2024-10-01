package com.be_planfortrips.entity;

import com.amadeus.resources.FlightOfferSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightOfferSearchDTO {
    private List<FlightOfferSearch.Itinerary> itineraries;  // Tạo lớp ItineraryDTO tương ứng với Itinerary
    private String type;
    private String id;

    // Các trường khác cần thiết tương ứng với FlightOfferSearch
}
