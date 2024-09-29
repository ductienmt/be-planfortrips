package com.be_planfortrips.responses;

import java.util.List;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineListResponse {
    List<AirlineResponse> list;
    int totalPage;
}
