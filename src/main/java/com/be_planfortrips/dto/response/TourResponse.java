package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Check;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourResponse {
    @JsonProperty("tour_id")
    Integer id;
    String title;
    @JsonProperty("route")
    Route route;
    @JsonProperty("number_people")
    Integer numberPeople;
    Double rating;
    @JsonProperty("total_price")
    Double totalPrice;
    Integer day;
    Integer night;
    @JsonProperty("is_active")
    boolean isActive;
    List<TagResponse> tags;
    @JsonProperty("hotel")
    Hotel hotel;
    @JsonProperty("car_company")
    CarCompany carCompany;
    @JsonProperty
    String note;
    List<Checkin> checkin;
    @JsonProperty("admin_username")
    String adminUsername;
    List<Image> images;
}
