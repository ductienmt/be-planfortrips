package com.be_planfortrips.dto.response;
import com.be_planfortrips.entity.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarResponse {
    @JsonProperty("car_company_id")
    Integer id;
    String name;
    String phoneNumber;
    Integer enterpriseId;
    Integer rating;
    List<Image> imageList ;
}
