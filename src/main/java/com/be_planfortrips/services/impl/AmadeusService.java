package com.be_planfortrips.services.impl;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.*;
import com.be_planfortrips.dto.AirlineDto;
import com.be_planfortrips.services.interfaces.IAmadeusService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AmadeusService implements IAmadeusService {
    private final Amadeus amadeus;
    public AmadeusService(@Value("${amadeus.id}") String clientId,
                          @Value("${amadeus.secret}") String clientSecret) {
        this.amadeus = Amadeus.builder(clientId, clientSecret).build();
    }

    @Override
    public List<Airline> getAirlines() throws ResponseException {
        List<Airline> airlines = new ArrayList<>();
        try {
                Airline[] airlineArray = amadeus.
                        referenceData.airlines.get();
            if (airlineArray != null) {
                airlines.addAll(Arrays.asList(airlineArray));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return airlines;
    }

    @Override
    public List<Location> getLocations(String keyword) throws ResponseException {
        List<Location> locations = new ArrayList<>();
        try {
            Location[] locationsArray = amadeus.referenceData.locations.get(Params
                    .with("keyword", keyword)
                    .and("subType", "AIRPORT,CITY"));

            if (locationsArray != null) {
                locations.addAll(Arrays.asList(locationsArray));
            }
        } catch (ResponseException e) {
            e.printStackTrace(); // Ghi log lỗi
        }
        return locations;
    }
}
