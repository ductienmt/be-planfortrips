package com.be_planfortrips.services.interfaces;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.*;

import java.util.List;

public interface IAmadeusService {
    List<Airline> getAirlines() throws ResponseException;
    List<Location> getLocations(String keyword)throws  ResponseException;
    List<HotelOffer> getHotels() throws ResponseException;

}
