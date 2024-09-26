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

public class Airport {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)Long id;String airportName;String airportCode;String city;String address;String country;@OneToMany
    List<Flight> flights;
}
