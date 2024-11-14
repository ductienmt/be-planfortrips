package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.BookingHotel;
import com.be_planfortrips.entity.Status;
import com.be_planfortrips.entity.Ticket;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.List;
import java.util.Map;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PlanResponseDetail {
    Long hotel_id;
    String hotel_name;
    List<Map<String, Object>> rooms;

    BigDecimal transport_price;
    Status status_transport;
    Map<String, Object> schedule_transport;
//    List<String> seats;

    Map<String, Object> routes;

    Map<String, Object> vehicle;

}
