package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tickets")
public class Ticket extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    Seat seat;

    @Column(name = "ticket_code", length = 10)
    String ticketCode;

    @Column(name = "price", precision = 10, scale = 2)
    BigDecimal price;

    @Column(name = "status", columnDefinition = "status_booking")
    Status status;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    String notes;

    @Column(name = "oneWay")
    Boolean oneWay;
}
