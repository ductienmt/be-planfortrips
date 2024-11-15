package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String description;
    String destination;
    @JsonProperty("number_people")
    Integer numberPeople;
    Double rating;
    @JsonProperty("total_price")
    Double totalPrice;
    Integer day;
    Integer night;
    boolean isActive;
    List<TagResponse> tags;
    @JsonProperty("hotel")
    Hotel hotel;
    @JsonProperty("room_list_available")
    List<Room> roomListAvailable;
    @JsonProperty("car_company")
    CarCompany carCompany;
    @JsonProperty("schedule")
    Schedule schedule;
    @JsonProperty("schedule_seat_list_available")
    List<ScheduleSeat> scheduleSeatListAvailable;
    @JsonProperty
    String note;
    @JsonProperty("admin_username")
    String adminUsername;
    List<Image> images;
}
