package com.be_planfortrips.dto.response.rsTourResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourTagResponse {

    UUID id;
    String name;
    String description;
}
