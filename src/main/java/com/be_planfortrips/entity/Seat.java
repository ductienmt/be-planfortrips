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
//@Table(name = "seats")
//public class Seat {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "seat_id", nullable = false)
//    Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "airplane_id")
//    Airplane airplane;
//
//    @Column(name = "seat_number", length = 10)
//    String seatNumber;
//
//
//    @Column(name = "status", columnDefinition = "status_seat")
//    Status status;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "flight_id")
//    Flight flight;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "class_id")
//    TicketClass ticketClass;
//
//    @OneToMany(mappedBy = "seat", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    List<Ticket> tickets;
//}
