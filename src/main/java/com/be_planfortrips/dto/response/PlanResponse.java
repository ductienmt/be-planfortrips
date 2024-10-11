package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Status;
import com.be_planfortrips.entity.StatusPlan;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanResponse {
    Integer plan_id;
    String plan_name;
    Timestamp start_date;
    Timestamp end_date;
    BigDecimal budget;
    BigDecimal total_price;
    StatusPlan status;
    String destination;
    String origin_location;
    Integer numberPeople;

    Integer hotel_id;
    String hotel_name;

    String room_id;
    String room_name;
    BigDecimal room_price;

    Timestamp check_in;
    Timestamp check_out;
    Status status_hotel;
    BigDecimal hotel_price;

    Integer ticket_id;
    BigDecimal transport_price;
    Status status_transport;
    Timestamp transport_date;

    Integer schedule_id;
    Timestamp departure_time;
    Timestamp arrival_time;

    Integer route_id;
    String origin_station;
    String destination_station;

    String vehicle_code;
    String plate_number;

    Integer car_company_id;
    String car_company_name;

}
