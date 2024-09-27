package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String flightName;
    String flightCode;
    @ManyToOne
    @JoinColumn(name = "departure_airport")
    Airport departureAirport;
    @ManyToOne
    @JoinColumn(name = "arrival_airport")
    Airport arrivalAirport;
    Date departureTime;
    Date arrivalTime;
    @ManyToOne
    @JoinColumn(name = "airplane_id")
    Airplane airplane;
    @OneToMany
    List<Seat> seats;
}
