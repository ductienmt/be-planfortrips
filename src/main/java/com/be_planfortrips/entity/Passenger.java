package com.be_planfortrips.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Passenger {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)Long id;String passengerName;
    Date birthDay;boolean gender;String citizenCard;String phoneNumber;String email;@OneToOne @JoinColumn(name = "booking_id") BookingPlane bookingPlane;
}
