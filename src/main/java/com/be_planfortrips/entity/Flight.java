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
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id", nullable = false)
    Long id;

    @Column(name = "flight_name", length = 100)
    String flightName;

    @Column(name = "flight_code", length = 10)
    String flightCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport")
    Airport departureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport")
    Airport arrivalAirport;

    @Column(name = "departure_time")
    Date departureTime;

    @Column(name = "arrival_time")
    Date arrivalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id")
    Airline airline;

    @OneToMany
    List<Seat> seats;
}
