<<<<<<< HEAD:src/main/java/com/be_planfortrips/dtos/response/AirlineResponse.java
package com.be_planfortrips.dto.response;
import com.be_planfortrips.entity.Airplane;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirlineResponse {
    String airlineName;
    String airlineCode;
    String airlineCountry;
    Long enterpriseId;
    List<Airplane> airplanes;
}
=======
//package com.be_planfortrips.responses;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class AirlineResponse {
//    String airlineName;
//    String airlineCode;
//    String airlineCountry;
//    Long enterpriseId;
////    List<Airplane> airplanes;
//}
>>>>>>> a2b8a3077b0c703f7ee7dd0c64fe44094e080841:src/main/java/com/be_planfortrips/responses/AirlineResponse.java
