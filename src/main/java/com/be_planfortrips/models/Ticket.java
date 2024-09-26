package com.be_planfortrips.models;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Ticket {
@Id@GeneratedValue(strategy = GenerationType.IDENTITY)Long id;@ManyToOne@JoinColumn(name = "seat_id") Seat seat;String ticketCode;Double price;
}
