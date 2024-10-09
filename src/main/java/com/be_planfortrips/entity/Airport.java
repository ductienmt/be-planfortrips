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
//@Table(name = "airports")
//public class Airport {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "airport_id", nullable = false)
//    Long id;
//
//    @Column(name = "airport_name", length = 100)
//    String airportName;
//
//    @Column(name = "airport_code", length = 5)
//    String airportCode;
//
//    @Column(name = "city", length = 100)
//    String city;
//
//    @Column(name = "address", length = Integer.MAX_VALUE)
//    String address;
//
//    @Column(name = "country", length = 20)
//    String country;
//}
