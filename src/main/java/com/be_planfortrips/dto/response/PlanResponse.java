package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Image;
import com.be_planfortrips.entity.Status;
import com.be_planfortrips.entity.StatusPlan;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanResponse {
    Long plan_id;
    String plan_name;
    Timestamp start_date;
    Timestamp end_date;
    BigDecimal budget;
    BigDecimal total_price;
    StatusPlan status;
    String destination;
    String origin_location;
    Integer numberPeople;

    Long hotel_id;
    String hotel_name;

    Long room_id;
    String room_name;
    BigDecimal room_price;

    Timestamp check_in;
    Timestamp check_out;
    Status status_hotel;
    BigDecimal hotel_price;

    Long ticket_id;
    BigDecimal transport_price;
    Status status_transport;
    Timestamp transport_date;

    Long schedule_id;
    Timestamp departure_time;
    Timestamp arrival_time;

    Long route_id;
    String origin_station;
    String destination_station;

    String vehicle_code;
    String plate_number;

    Long car_company_id;
    String car_company_name;

}
