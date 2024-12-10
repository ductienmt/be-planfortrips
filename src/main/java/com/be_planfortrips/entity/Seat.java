package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

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
    Vehicle vehicle;

    @Column(name = "seat_number", length = 5)
    String seatNumber;
    @OneToMany(mappedBy = "seat",cascade = CascadeType.REMOVE,orphanRemoval = true)
    @JsonIgnore
    List<ScheduleSeat> scheduleSeats;
//    @Column(name = "status")
//    @Enumerated(EnumType.STRING)
//    StatusSeat status = StatusSeat.Empty;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "ticket_seats",
            joinColumns = @JoinColumn(name = "seat_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id"))
    @JsonIgnore
    List<Ticket> tickets;
}