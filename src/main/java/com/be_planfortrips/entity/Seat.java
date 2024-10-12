package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_code")
    @JsonBackReference
    Vehicle vehicleCode;

    @Column(name = "seat_number", length = 5)
    String seatNumber;

    @ColumnDefault("Empty")
    @Column(name = "status", columnDefinition = "status_seat")
//    @Enumerated(EnumType.STRING)
    StatusSeat status;

}