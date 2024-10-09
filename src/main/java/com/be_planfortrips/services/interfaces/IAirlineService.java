<<<<<<< HEAD
package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.AirlineDto;
import com.be_planfortrips.entity.Airline;
import com.be_planfortrips.dto.response.AirlineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IAirlineService {
    Airline saveAirline(AirlineDto airlineDto) throws Exception;
    Page<AirlineResponse> getAirlines(PageRequest request);
    Airline updateAirline(Long id,AirlineDto airlineDto) throws Exception;
    AirlineResponse getAirlineById(Long id) throws Exception;
    void deleteAirlineById(Long id);
}
=======
//package com.be_planfortrips.services.interfaces;
//
//import com.be_planfortrips.dto.AirlineDto;
//import com.be_planfortrips.entity.Airline;
//import com.be_planfortrips.responses.AirlineResponse;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//
//public interface IAirlineService {
//    Airline saveAirline(AirlineDto airlineDto) throws Exception;
//    Page<AirlineResponse> getAirlines(PageRequest request);
//    Airline updateAirline(Long id,AirlineDto airlineDto) throws Exception;
//    AirlineResponse getAirlineById(Long id) throws Exception;
//    void deleteAirlineById(Long id);
//}
>>>>>>> a2b8a3077b0c703f7ee7dd0c64fe44094e080841
