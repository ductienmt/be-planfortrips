package com.be_planfortrips.dto;


import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Traveler;
import com.be_planfortrips.entity.FlightOfferSearchDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingRequest {
    private List<FlightOfferSearchDTO> flightOffers;
    private List<Traveler> travelers;

    // Getters and Setters
}

