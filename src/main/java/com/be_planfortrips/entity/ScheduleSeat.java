package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "schedule_seats")
public class ScheduleSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Integer id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @JsonBackReference
     Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    @JsonBackReference
     Seat seat;

    @Column(name = "seat_number")
     String seatNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
     StatusSeat status;
}

