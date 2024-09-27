package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id", nullable = false)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_id")
    Airplane airplane;

    @Column(name = "seat_number", length = 10)
    String seatNumber;

    @ColumnDefault("Empty")
    @Column(name = "status")
    Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    Flight flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    TicketClass ticketClass;

    @OneToMany
    List<Ticket> tickets;
}
