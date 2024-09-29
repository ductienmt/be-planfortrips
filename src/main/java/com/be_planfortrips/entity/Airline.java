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
@Table(name = "airlines")
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "airline_name", length = 100)
    String airlineName;

    @Column(name = "airline_code", length = 5)
    String airlineCode;

    @Column(name = "airline_country", length = 100)
    String airlineCountry;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id")
    AccountEnterprise accountEnterprise;

    @OneToMany
    List<Airplane> airplanes;
}
