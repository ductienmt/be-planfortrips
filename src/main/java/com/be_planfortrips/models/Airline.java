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

public class Airline {
@Id@GeneratedValue(strategy = GenerationType.IDENTITY)Long id;String airlineName;String airlineCode;String countryAirline;@ManyToOne@JoinColumn(name = "enterprise_id") AccountEnterprise accountEnterprise;@OneToMany
    List<Airplane> airplanes;
}
