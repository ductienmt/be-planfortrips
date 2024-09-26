package com.be_planfortrips.models;
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

public class Airplane {
@Id@GeneratedValue(strategy = GenerationType.IDENTITY)Long id;String model;int seatLimit;@ManyToOne@JoinColumn(name = "airline_id") Airline airline;@OneToMany
    List<Flight> flights;
}
