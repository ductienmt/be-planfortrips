package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Image;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanResponse {
    String destination;
    String transport_name;
    String transport_type;
    Long transport_id;
    Long transport_route;
    String transport_departure;
    String transport_arrival;
    LocalDateTime transport_departure_time;
    LocalDateTime transport_arrival_time;

    String hotel_name;
    String hotel_address;
    String hotel_phone;
    String hotel_email;
    LocalDateTime hotel_checkin;
    LocalDateTime hotel_checkout;
    Long hotel_image;
    String hotel_type;
    String hotel_room; // nếu là homestay thì không có thông tin phòng

    String restaurant_name;
    String restaurant_address;
    String restaurant_phone;

    String checkin_name;
    String checkin_address;

}
