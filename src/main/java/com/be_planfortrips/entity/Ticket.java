package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "total_price", precision = 10, scale = 2)
    BigDecimal totalPrice;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "booking_time")
    Instant bookingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    Payment payment;

    @Column(name = "status", columnDefinition = "status_booking")
//    @Enumerated(EnumType.STRING)
    Status status;
    @ManyToMany
    @JoinTable(name = "ticket_seats",
    joinColumns = @JoinColumn(name = "ticket_id"),
    inverseJoinColumns = @JoinColumn(name = "seat_id"))
    List<Seat> seats;
}