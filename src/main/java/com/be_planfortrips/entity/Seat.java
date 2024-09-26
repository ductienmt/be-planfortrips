package com.be_planfortrips.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Seat {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)Long id;@ManyToOne@JoinColumn(name = "airplane_id") Airplane airplane;String seatNumber;Status status;@ManyToOne@JoinColumn(name = "flight_id") Flight flight;@ManyToOne@JoinColumn(name = "class_id") TicketClass ticketClass;@OneToMany
    List<Ticket> tickets;
}
