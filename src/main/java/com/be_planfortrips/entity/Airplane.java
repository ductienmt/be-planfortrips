//package com.be_planfortrips.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Builder
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Table(name = "airplanes")
//public class Airplane {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
//
//    @Column(name = "model", length = 50)
//    String model;
//
//    @Column(name = "seat_capacity")
//    int seatLimit;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "airline_id")
//    Airline airline;
//
//    @OneToMany(mappedBy = "airplane", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    List<Flight> flights;
//}