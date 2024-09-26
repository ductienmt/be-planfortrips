package com.be_planfortrips.models;
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

public class BookingPlane {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)Long id;@ManyToOne@JoinColumn(name = "ticket_id") Ticket ticket;@ManyToOne@JoinColumn(name = "user_id") User user;
    Date bookDate;Status status;Double totalPrice;@ManyToOne@JoinColumn(name = "service_id") Service service;@ManyToOne@JoinColumn(name = "payment_id") Payment payment;
}
