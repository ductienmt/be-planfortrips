package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "schedules")
public class Schedule extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id")
    Route route;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_code")
    @JsonIgnore
    Vehicle vehicleCode;

    @Column(name = "price_for_one_seat", precision = 10, scale = 2)
    BigDecimal price_for_one_seat;

    @Column(name = "departure_time", columnDefinition = "timestamp without time zone")
    LocalDateTime departureTime;

    @Column(name = "arrival_time", columnDefinition = "timestamp without time zone")
    LocalDateTime arrivalTime;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<ScheduleSeat> scheduleSeats;

}